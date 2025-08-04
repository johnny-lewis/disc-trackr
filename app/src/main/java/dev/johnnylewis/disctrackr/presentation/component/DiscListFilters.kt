package dev.johnnylewis.disctrackr.presentation.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.johnnylewis.disctrackr.R
import dev.johnnylewis.disctrackr.presentation.model.DiscFilterState
import dev.johnnylewis.disctrackr.presentation.util.LightDarkPreview
import dev.johnnylewis.disctrackr.presentation.util.PreviewHelper
import dev.johnnylewis.disctrackr.presentation.util.getName
import dev.johnnylewis.disctrackr.presentation.viewmodel.DiscScreenViewModel

@Composable
fun DiscListFilters(
  modifier: Modifier = Modifier,
  state: DiscFilterState,
  onEvent: (DiscScreenViewModel.Event) -> Unit,
) {
  Row(
    modifier = modifier
      .horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    var isFormatExpanded by remember { mutableStateOf(false) }
    var isCountryExpanded by remember { mutableStateOf(false) }
    var isDistributorExpanded by remember { mutableStateOf(false) }

    DiscFilterChip(
      label = state.selection.format?.getName()
        ?: stringResource(R.string.disc_screen_filter_format),
      options = state.options.formats.map { FilterChipOption(label = it.getName(), data = it) },
      isSelected = state.selection.format != null,
      isExpanded = isFormatExpanded,
      onExpandedChanged = { isFormatExpanded = it },
      onOptionSelected = { onEvent(DiscScreenViewModel.Event.FormatFilterChanged(it)) },
    )
    DiscFilterChip(
      label = state.selection.country?.name
        ?: stringResource(R.string.disc_screen_filter_country),
      options = state.options.countries.map { FilterChipOption(label = it.name, data = it) },
      isSelected = state.selection.country != null,
      isExpanded = isCountryExpanded,
      onExpandedChanged = { isCountryExpanded = it },
      onOptionSelected = { onEvent(DiscScreenViewModel.Event.CountryFilterChanged(it)) },
    )
    DiscFilterChip(
      label = state.selection.distributor
        ?: stringResource(R.string.disc_screen_filter_distributor),
      options = state.options.distributors.map { FilterChipOption(label = it, data = it) },
      isSelected = state.selection.distributor != null,
      isExpanded = isDistributorExpanded,
      onExpandedChanged = { isDistributorExpanded = it },
      onOptionSelected = { onEvent(DiscScreenViewModel.Event.DistributorFilterChanged(it)) },
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> DiscFilterChip(
  label: String,
  options: List<FilterChipOption<T>>,
  isSelected: Boolean,
  isExpanded: Boolean,
  onExpandedChanged: (Boolean) -> Unit,
  onOptionSelected: (T?) -> Unit,
) {
  ExposedDropdownMenuBox(
    expanded = isExpanded,
    onExpandedChange = {},
  ) {
    FilterChip(
      modifier = Modifier
        .menuAnchor(
          type = MenuAnchorType.PrimaryEditable,
          enabled = true,
        ),
      label = {
        Text(
          text = label,
          color = if (isSelected) {
            MaterialTheme.colorScheme.onPrimaryContainer
          } else {
            MaterialTheme.colorScheme.onBackground
          },
          maxLines = 1,
        )
      },
      selected = isSelected,
      onClick = {
        if (isSelected) {
          onOptionSelected(null)
          onExpandedChanged(false)
        } else {
          onExpandedChanged(!isExpanded)
        }
      },
      trailingIcon = getCloseIcon(isSelected),
    )
    ExposedDropdownMenu(
      modifier = Modifier
        .width(IntrinsicSize.Min)
        .heightIn(max = 300.dp),
      expanded = isExpanded,
      onDismissRequest = { onExpandedChanged(false) },
    ) {
      options.forEach { option ->
        DropdownMenuItem(
          text = { Text(option.label) },
          onClick = {
            onExpandedChanged(false)
            onOptionSelected(option.data)
          },
          contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
        )
      }
    }
  }
}

@Composable
private fun getCloseIcon(isSelected: Boolean): (@Composable () -> Unit)? =
  if (isSelected) {
    {
      Icon(
        modifier = Modifier
          .size(16.dp),
        tint = MaterialTheme.colorScheme.onPrimaryContainer,
        painter = painterResource(R.drawable.close),
        contentDescription = null,
      )
    }
  } else {
    null
  }

private data class FilterChipOption<T>(
  val label: String,
  val data: T,
)

@LightDarkPreview
@Composable
private fun DiscListFiltersPreview() = PreviewHelper.Component {
  DiscListFilters(
    state = DiscFilterState(),
    onEvent = {},
  )
}
