const estado = document.getElementById('estado');
const botonTema = document.getElementById('tema');
const cartelera = document.getElementById('cartelera');
const textoSeleccion = document.getElementById('seleccion');
const lista = document.getElementById('lista');
const formulario = document.getElementById('form-tarea');
const entrada = document.getElementById('input-tarea');
const bitacora = document.getElementById('bitacora');
const botonBitacora = document.getElementById('ver-bitacora');
const toast = document.getElementById('toast');

const CLAVE_SELECCION = 'cartelera.seleccion';
const MAXIMO_BITACORA = 100;

let seleccion = leerSeleccion();
let titulosEnLista = new Set();
let temporizadorToast;

function mostrarToast(texto) {
  toast.textContent = texto;
  toast.classList.add('visible');
  clearTimeout(temporizadorToast);
  temporizadorToast = setTimeout(() => toast.classList.remove('visible'), 2500);
}

function leerSeleccion() {
  try {
    const guardada = JSON.parse(localStorage.getItem(CLAVE_SELECCION));
    return guardada && guardada.titulo ? guardada : null;
  } catch (error) {
    return null;
  }
}

function guardarSeleccion(pelicula) {
  try {
    localStorage.setItem(CLAVE_SELECCION, JSON.stringify(pelicula));
  } catch (error) {
    console.warn('No se pudo guardar la película elegida');
  }
}

function actualizarBotonBitacora() {
  const accion = bitacora.hidden ? 'Mostrar' : 'Ocultar';
  botonBitacora.textContent = `${accion} (${bitacora.children.length})`;
  botonBitacora.setAttribute('aria-expanded', String(!bitacora.hidden));
}

botonBitacora.addEventListener('click', () => {
  bitacora.hidden = !bitacora.hidden;
  actualizarBotonBitacora();
});

document.addEventListener('bitacora', ({ detail }) => {
  const linea = document.createElement('li');
  linea.className = detail.direccion === 'WEB → APP' ? 'sale' : 'entra';
  linea.classList.toggle('sin-enviar', !detail.enviada);

  const resumen = document.createElement('span');
  resumen.textContent = `${detail.direccion}  ${detail.detalle}`;

  const completa = document.createElement('code');
  completa.textContent = detail.carta;

  linea.append(resumen, completa);
  linea.addEventListener('click', () => linea.classList.toggle('abierta'));
  bitacora.prepend(linea);
  while (bitacora.children.length > MAXIMO_BITACORA) bitacora.lastElementChild.remove();
  actualizarBotonBitacora();
});

function aplicarTema(tema) {
  const oscuro = tema === 'oscuro';
  const ayuda = oscuro ? 'Cambiar a modo claro' : 'Cambiar a modo oscuro';
  document.body.classList.toggle('oscuro', oscuro);
  botonTema.textContent = oscuro ? '☀️' : '🌙';
  botonTema.setAttribute('aria-label', ayuda);
  botonTema.title = ayuda;
}

function normalizar(titulo) {
  return titulo.trim().toLowerCase();
}

function tituloTarea(pelicula) {
  return `Ver: ${pelicula.titulo}`;
}

function estaEnLista(pelicula) {
  return titulosEnLista.has(normalizar(tituloTarea(pelicula)));
}

function mostrarSeleccion() {
  textoSeleccion.classList.toggle('activa', Boolean(seleccion));
  textoSeleccion.textContent = seleccion
    ? `Elegiste: ${seleccion.titulo}`
    : 'Toca un póster para elegir una película. La app puede preguntarte cuál elegiste.';
}

function marcarTarjetas() {
  cartelera.querySelectorAll('.pelicula').forEach((tarjeta) => {
    const pelicula = { titulo: tarjeta.dataset.titulo };
    const enLista = estaEnLista(pelicula);
    const boton = tarjeta.querySelector('button');
    tarjeta.classList.toggle('elegida', Boolean(seleccion) && seleccion.titulo === pelicula.titulo);
    boton.classList.toggle('en-lista', enLista);
    boton.textContent = enLista ? '✓ En tu lista' : '+ Mi lista';
  });
}

function crearTarjeta(pelicula) {
  const tarjeta = document.createElement('article');
  tarjeta.className = 'pelicula';
  tarjeta.dataset.titulo = pelicula.titulo;

  const poster = document.createElement('img');
  poster.src = pelicula.poster;
  poster.alt = pelicula.titulo;
  poster.loading = 'lazy';

  const titulo = document.createElement('h3');
  titulo.textContent = pelicula.titulo;

  const boton = document.createElement('button');
  boton.textContent = '+ Mi lista';
  boton.addEventListener('click', () => {
    if (estaEnLista(pelicula)) {
      mostrarToast('Ya está en tu lista');
      return;
    }
    agregarALista(tituloTarea(pelicula), boton);
  });

  tarjeta.addEventListener('click', () => elegir(pelicula));
  tarjeta.append(poster, titulo, boton);
  return tarjeta;
}

function dibujarCartelera(peliculas) {
  if (!peliculas.length) {
    cartelera.textContent = 'La app no devolvió películas';
    return;
  }
  cartelera.replaceChildren(...peliculas.map(crearTarjeta));
  marcarTarjetas();
}

function elegir(pelicula) {
  seleccion = { titulo: pelicula.titulo, poster: pelicula.poster };
  guardarSeleccion(seleccion);
  mostrarSeleccion();
  marcarTarjetas();
}

function dibujarLista({ tareas = [] }) {
  titulosEnLista = new Set(tareas.map((tarea) => normalizar(tarea.titulo)));
  marcarTarjetas();
  if (!tareas.length) {
    const vacia = document.createElement('li');
    vacia.className = 'vacia';
    vacia.textContent = 'Tu lista está vacía';
    lista.replaceChildren(vacia);
    return;
  }
  lista.replaceChildren(...tareas.map((tarea) => {
    const item = document.createElement('li');
    item.textContent = tarea.titulo;
    if (tarea.completada) item.classList.add('vista');
    return item;
  }));
}

async function cargarCartelera() {
  cartelera.textContent = 'Pidiendo películas a la app…';
  try {
    const { peliculas = [] } = await Puente.pedir('obtenerPeliculas');
    dibujarCartelera(peliculas);
  } catch (error) {
    cartelera.textContent = Puente.disponible
      ? `No se pudo cargar la cartelera: ${error.message}`
      : 'Abre esta página desde la app: la web no tiene acceso a TMDB ni a Firebase, se los pide a la app por mensajes.';
  }
}

async function agregarALista(titulo, boton) {
  if (boton) boton.disabled = true;
  try {
    const respuesta = await Puente.pedir('agregarALista', { titulo });
    mostrarToast(`Agregada a tu lista: ${respuesta.titulo}`);
  } catch (error) {
    mostrarToast(error.message);
  } finally {
    if (boton) boton.disabled = false;
  }
}

Puente.alRecibirEvento('listaActualizada', dibujarLista);

Puente.alRecibirEvento('cambiarTema', ({ tema }) => aplicarTema(tema));

Puente.alRecibirPeticion('obtenerSeleccion', () => {
  if (!seleccion) throw new Error('Todavía no elegiste ninguna película en la web');
  return { titulo: seleccion.titulo, poster: seleccion.poster };
});

botonTema.addEventListener('click', () => {
  const tema = document.body.classList.contains('oscuro') ? 'claro' : 'oscuro';
  aplicarTema(tema);
  Puente.emitir('cambiarTema', { tema });
});

formulario.addEventListener('submit', (evento) => {
  evento.preventDefault();
  const titulo = entrada.value.trim();
  if (!titulo) return;
  entrada.value = '';
  agregarALista(titulo);
});

document.querySelectorAll('[data-pantalla]').forEach((boton) => {
  boton.addEventListener('click', () => {
    if (!Puente.emitir('abrirPantalla', { pantalla: boton.dataset.pantalla })) mostrarToast(Puente.SIN_APP);
  });
});

mostrarSeleccion();
actualizarBotonBitacora();

if (Puente.disponible) {
  estado.textContent = 'Conectada';
  estado.classList.add('conectada');
} else {
  estado.textContent = 'Fuera de la app';
  lista.querySelector('.vacia').textContent = 'Sin la app no hay lista: la web la recibe por mensajes';
}

Puente.emitir('listo', { pagina: 'cartelera' });
cargarCartelera();
