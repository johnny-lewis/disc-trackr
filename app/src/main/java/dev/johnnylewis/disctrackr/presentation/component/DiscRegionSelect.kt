package dev.johnnylewis.disctrackr.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.johnnylewis.disctrackr.R
import dev.johnnylewis.disctrackr.presentation.model.DiscFormat
import dev.johnnylewis.disctrackr.presentation.model.DiscRegion
import dev.johnnylewis.disctrackr.presentation.util.hasRegions

@Composable
fun DiscRegionSelect(
  modifier: Modifier = Modifier,
  selectedFormat: DiscFormat,
  selectedRegions: Set<DiscRegion>,
  onRegionSelected: (DiscRegion, Boolean) -> Unit,
) {
  if (!selectedFormat.hasRegions()) {
    return
  }
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(4.dp),
  ) {
    Text(
      text = stringResource(R.string.disc_screen_form_regions_title),
      style = MaterialTheme.typography.bodyLarge,
      color = MaterialTheme.colorScheme.onBackground,
    )
    FlowRow(
      modifier = Modifier
        .fillMaxWidth(),
    ) {
      selectedFormat.getRegions().forEach { region ->
        Row(
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Checkbox(
            checked = selectedRegions.contains(region),
            onCheckedChange = { onRegionSelected(region, it) },
          )
          Text(
            text = region.getTitle(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground,
          )
        }
      }
    }
  }
}

private fun DiscFormat.getRegions(): List<DiscRegion> =
  DiscRegion.entries.filter { it.format == this }

@Composable
private fun DiscRegion.getTitle(): String =
  when (this) {
    DiscRegion.ZERO -> stringResource(R.string.disc_screen_form_region_zero)
    DiscRegion.ONE -> stringResource(R.string.disc_screen_form_region_one)
    DiscRegion.TWO -> stringResource(R.string.disc_screen_form_region_two)
    DiscRegion.THREE -> stringResource(R.string.disc_screen_form_region_three)
    DiscRegion.FOUR -> stringResource(R.string.disc_screen_form_region_four)
    DiscRegion.FIVE -> stringResource(R.string.disc_screen_form_region_five)
    DiscRegion.SIX -> stringResource(R.string.disc_screen_form_region_six)
    DiscRegion.A -> stringResource(R.string.disc_screen_form_region_a)
    DiscRegion.B -> stringResource(R.string.disc_screen_form_region_b)
    DiscRegion.C -> stringResource(R.string.disc_screen_form_region_c)
    DiscRegion.ALL -> stringResource(R.string.disc_screen_form_region_all)
  }
