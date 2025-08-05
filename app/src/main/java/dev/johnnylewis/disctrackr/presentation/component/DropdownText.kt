package dev.johnnylewis.disctrackr.presentation.component

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.johnnylewis.disctrackr.presentation.model.DropdownOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T>DropdownText(
  modifier: Modifier = Modifier,
  selected: T,
  options: List<DropdownOption<T>>,
  onValueChange: (T) -> Unit,
) {
  var isExpanded by remember { mutableStateOf(false) }
  ExposedDropdownMenuBox(
    expanded = isExpanded,
    onExpandedChange = { isExpanded = it },
  ) {
    TextField(
      modifier = modifier
        .menuAnchor(type = MenuAnchorType.PrimaryEditable),
      value = options.firstOrNull { it.value == selected }?.title ?: "",
      onValueChange = {},
      readOnly = true,
      trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
    )
    ExposedDropdownMenu(
      expanded = isExpanded,
      onDismissRequest = { isExpanded = false },
    ) {
      options.forEach { option ->
        DropdownMenuItem(
          text = {
            Text(
              text = option.title,
              style = MaterialTheme.typography.labelLarge,
            )
          },
          onClick = {
            onValueChange(option.value)
            isExpanded = false
          },
          contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
        )
      }
    }
  }
}
