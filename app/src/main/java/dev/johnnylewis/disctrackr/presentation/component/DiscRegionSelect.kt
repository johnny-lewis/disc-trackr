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
import dev.johnnylewis.disctrackr.presentation.model.DiscFormResult
import dev.johnnylewis.disctrackr.presentation.util.getRegions
import dev.johnnylewis.disctrackr.presentation.util.hasRegions

@Composable
fun DiscRegionSelect(
  modifier: Modifier = Modifier,
  selectedFormat: DiscFormResult.DiscFormFormat,
  selectedRegions: Set<DiscFormResult.DiscFormRegion>,
  onRegionSelected: (DiscFormResult.DiscFormRegion, Boolean) -> Unit,
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
          selectedRegions.isOptionDisabled(region).let { isDisabled ->
            Checkbox(
              enabled = !isDisabled,
              checked = selectedRegions.contains(region),
              onCheckedChange = { onRegionSelected(region, it) },
            )
            Text(
              text = region.getTitle(),
              style = MaterialTheme.typography.labelLarge,
              color = MaterialTheme.colorScheme.onBackground.copy(
                alpha = if (isDisabled) 0.4f else 1f,
              ),
            )
          }
        }
      }
    }
  }
}

private fun Set<DiscFormResult.DiscFormRegion>.isOptionDisabled(
  region: DiscFormResult.DiscFormRegion,
): Boolean =
  any {
    it == DiscFormResult.DiscFormRegion.ALL || it == DiscFormResult.DiscFormRegion.ZERO
  } && region != DiscFormResult.DiscFormRegion.ALL && region != DiscFormResult.DiscFormRegion.ZERO

@Composable
private fun DiscFormResult.DiscFormRegion.getTitle(): String =
  when (this) {
    DiscFormResult.DiscFormRegion.ZERO -> stringResource(R.string.disc_screen_form_region_zero)
    DiscFormResult.DiscFormRegion.ONE -> stringResource(R.string.disc_screen_form_region_one)
    DiscFormResult.DiscFormRegion.TWO -> stringResource(R.string.disc_screen_form_region_two)
    DiscFormResult.DiscFormRegion.THREE -> stringResource(R.string.disc_screen_form_region_three)
    DiscFormResult.DiscFormRegion.FOUR -> stringResource(R.string.disc_screen_form_region_four)
    DiscFormResult.DiscFormRegion.FIVE -> stringResource(R.string.disc_screen_form_region_five)
    DiscFormResult.DiscFormRegion.SIX -> stringResource(R.string.disc_screen_form_region_six)
    DiscFormResult.DiscFormRegion.A -> stringResource(R.string.disc_screen_form_region_a)
    DiscFormResult.DiscFormRegion.B -> stringResource(R.string.disc_screen_form_region_b)
    DiscFormResult.DiscFormRegion.C -> stringResource(R.string.disc_screen_form_region_c)
    DiscFormResult.DiscFormRegion.ALL -> stringResource(R.string.disc_screen_form_region_all)
  }
