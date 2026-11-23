const Puente = (() => {
  const nativo = window.AppNativa;
  const pendientes = new Map();
  const manejadoresEvento = {};
  const manejadoresPeticion = {};
  let ultimoTicket = 0;

  const SIN_APP = 'Solo funciona dentro de la app';

  function anotar(direccion, carta, enviada = true) {
    const partes = [carta.type, carta.action];
    if (carta.id) partes.push(`#${carta.id}`);
    if (carta.type === 'response') partes.push(carta.ok ? 'ok' : `error: ${carta.error}`);
    if (!enviada) partes.push('no enviada (fuera de la app)');
    const detalle = partes.join(' · ');
    console.log(`${direccion} ${JSON.stringify(carta)}${enviada ? '' : ' (no enviada)'}`);
    document.dispatchEvent(new CustomEvent('bitacora', {
      detail: { direccion, detalle, enviada, carta: JSON.stringify(carta) }
    }));
  }

  function enviar(carta) {
    anotar('WEB → APP', carta, Boolean(nativo));
    if (!nativo) return false;
    nativo.postMessage(JSON.stringify(carta));
    return true;
  }

  function emitir(action, payload = {}) {
    return enviar({ type: 'event', action, payload });
  }

  function pedir(action, payload = {}, espera = 8000) {
    ultimoTicket += 1;
    const id = `w-${ultimoTicket}`;
    if (!nativo) {
      enviar({ type: 'request', action, id, payload });
      return Promise.reject(new Error(SIN_APP));
    }
    return new Promise((resolve, reject) => {
      const temporizador = setTimeout(() => {
        pendientes.delete(id);
        reject(new Error('La app no respondió a tiempo'));
      }, espera);
      pendientes.set(id, { resolve, reject, temporizador });
      enviar({ type: 'request', action, id, payload });
    });
  }

  function alRecibirEvento(action, manejador) {
    manejadoresEvento[action] = manejador;
  }

  function alRecibirPeticion(action, manejador) {
    manejadoresPeticion[action] = manejador;
  }

  function atenderRespuesta(carta) {
    const ticket = pendientes.get(carta.id);
    if (!ticket) return;
    pendientes.delete(carta.id);
    clearTimeout(ticket.temporizador);
    if (carta.ok) {
      ticket.resolve(carta.payload || {});
    } else {
      ticket.reject(new Error(carta.error || 'Error en la app'));
    }
  }

  function atenderEvento(carta) {
    const manejador = manejadoresEvento[carta.action];
    if (manejador) {
      manejador(carta.payload || {});
    } else {
      console.warn(`Evento sin manejador: ${carta.action}`);
    }
  }

  async function atenderPeticion(carta) {
    const respuesta = { type: 'response', action: carta.action, id: carta.id };
    try {
      const manejador = manejadoresPeticion[carta.action];
      if (!manejador) throw new Error(`La web no sabe responder ${carta.action}`);
      respuesta.payload = (await manejador(carta.payload || {})) || {};
      respuesta.ok = true;
    } catch (error) {
      respuesta.ok = false;
      respuesta.error = error.message;
    }
    enviar(respuesta);
  }

  function recibir(texto) {
    let carta;
    try {
      carta = JSON.parse(texto);
    } catch (error) {
      console.warn(`Carta ilegible: ${texto}`);
      return;
    }
    anotar('APP → WEB', carta);
    if (carta.type === 'response') atenderRespuesta(carta);
    else if (carta.type === 'event') atenderEvento(carta);
    else if (carta.type === 'request') atenderPeticion(carta);
  }

  if (nativo) {
    nativo.onmessage = (evento) => recibir(evento.data);
  }

  return {
    disponible: Boolean(nativo),
    SIN_APP,
    emitir,
    pedir,
    alRecibirEvento,
    alRecibirPeticion
  };
})();
