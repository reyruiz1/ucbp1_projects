package edu.ucb.project.signin.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRowExample() {
    val rows = 3
    val columns = 3
    FlowRow(
        modifier = Modifier.padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        maxItemsInEachRow = rows
    ) {
        val itemModifier = Modifier
            .padding(4.dp)
            .width(80.dp)
            .height(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Blue)
        
        repeat(rows * columns) {
            Spacer(modifier = itemModifier)
        }
    }
}

@Preview
@Composable
fun FlowRowExamplePreview() {
    MaterialTheme {
        FlowRowExample()
    }
}
