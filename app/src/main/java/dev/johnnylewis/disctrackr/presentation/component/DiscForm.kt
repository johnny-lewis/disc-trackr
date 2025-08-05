package dev.johnnylewis.disctrackr.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.johnnylewis.disctrackr.R
import dev.johnnylewis.disctrackr.presentation.model.Country
import dev.johnnylewis.disctrackr.presentation.model.DiscFormResult
import dev.johnnylewis.disctrackr.presentation.model.DiscFormat
import dev.johnnylewis.disctrackr.presentation.model.DropdownOption
import dev.johnnylewis.disctrackr.presentation.util.CountryUtil
import dev.johnnylewis.disctrackr.presentation.util.LightDarkPreview
import dev.johnnylewis.disctrackr.presentation.util.PreviewHelper
import dev.johnnylewis.disctrackr.presentation.util.getName
import dev.johnnylewis.disctrackr.presentation.util.noRippleClickable
import dev.johnnylewis.disctrackr.presentation.viewmodel.DiscFormViewModel

@Composable
fun DiscForm(
  modifier: Modifier = Modifier,
  viewModel: DiscFormViewModel = viewModel(),
  shouldClearState: Boolean,
  onStateCleared: () -> Unit,
  onSubmit: (DiscFormResult) -> Unit,
) {
  LaunchedEffect(shouldClearState) {
    if (shouldClearState) {
      viewModel.onEvent(DiscFormViewModel.Event.ClearState)
      onStateCleared()
    }
  }

  val state by viewModel.state.collectAsState()
  DiscFormContent(
    modifier = modifier,
    state = state,
    onEvent = viewModel::onEvent,
    onSubmit = onSubmit,
  )
}

@Composable
private fun DiscFormContent(
  modifier: Modifier = Modifier,
  state: DiscFormViewModel.State,
  onEvent: (DiscFormViewModel.Event) -> Unit,
  onSubmit: (DiscFormResult) -> Unit,
) {
  var isCountrySheetExpanded by rememberSaveable { mutableStateOf(false) }
  Column(
    modifier = modifier
      .padding(horizontal = 16.dp)
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    TextField(
      modifier = Modifier
        .fillMaxWidth(),
      value = state.name,
      onValueChange = { onEvent(DiscFormViewModel.Event.NameChanged(it)) },
      label = { Text(stringResource(R.string.disc_screen_form_name)) },
      keyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.Words,
      ),
    )
    DropdownText(
      modifier = Modifier
        .fillMaxWidth(),
      selected = state.format,
      options = listOf(
        DropdownOption(
          value = DiscFormat.DVD,
          title = DiscFormat.DVD.getName(),
        ),
        DropdownOption(
          value = DiscFormat.BLU_RAY,
          title = DiscFormat.BLU_RAY.getName(),
        ),
        DropdownOption(
          value = DiscFormat.UHD,
          title = DiscFormat.UHD.getName(),
        ),
      ),
      onValueChange = { onEvent(DiscFormViewModel.Event.FormatChanged(it)) },
    )
    DiscRegionSelect(
      modifier = Modifier
        .fillMaxWidth(),
      selectedFormat = state.format,
      selectedRegions = state.regions,
      onRegionSelected = { region, selected ->
        onEvent(DiscFormViewModel.Event.RegionSelected(region = region, selected = selected))
      },
    )
    CountrySelector(
      modifier = Modifier
        .fillMaxWidth(),
      selectedCountry = state.selectedCountry,
      onClick = { isCountrySheetExpanded = true },
      onClear = { onEvent(DiscFormViewModel.Event.CountryCleared) },
    )
    TextField(
      modifier = Modifier
        .fillMaxWidth(),
      value = state.distributor,
      onValueChange = { onEvent(DiscFormViewModel.Event.DistributorChanged(it)) },
      label = { Text(stringResource(R.string.disc_screen_form_distributor)) },
      keyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.Words,
      ),
    )
    TextField(
      modifier = Modifier
        .fillMaxWidth(),
      value = state.year,
      onValueChange = { onEvent(DiscFormViewModel.Event.YearChanged(it)) },
      label = { Text(stringResource(R.string.disc_screen_form_year)) },
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
      ),
    )
    TextField(
      modifier = Modifier
        .fillMaxWidth(),
      value = state.blurayId,
      onValueChange = { onEvent(DiscFormViewModel.Event.BlurayIdChanged(it)) },
      label = { Text(stringResource(R.string.disc_screen_form_bluray_id)) },
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
      ),
    )
    Spacer(modifier = Modifier.weight(1f))
    Button(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp),
      shape = RoundedCornerShape(6.dp),
      enabled = state.isFormValid,
      onClick = { onSubmit(state.mapToResult()) },
    ) {
      Text(
        text = stringResource(R.string.disc_screen_form_save),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSecondary,
        fontWeight = FontWeight.SemiBold,
      )
    }
  }
  CountryBottomSheet(
    isExpanded = isCountrySheetExpanded,
    onDismiss = { isCountrySheetExpanded = false },
    state = state,
    onFilterChanged = { onEvent(DiscFormViewModel.Event.CountryFilterChanged(it)) },
    onSelected = {
      onEvent(DiscFormViewModel.Event.CountrySelected(it))
      isCountrySheetExpanded = false
      onEvent(DiscFormViewModel.Event.CountryFilterCleared)
    },
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CountrySelector(
  modifier: Modifier = Modifier,
  selectedCountry: Country?,
  onClick: () -> Unit,
  onClear: () -> Unit,
) {
  val focusManager = LocalFocusManager.current
  TextField(
    modifier = modifier
      .onFocusEvent { focusState ->
        if (focusState.isFocused) {
          onClick()
          focusManager.clearFocus()
        }
      },
    value = selectedCountry?.name ?: "",
    onValueChange = {},
    readOnly = true,
    placeholder = { Text(text = stringResource(R.string.disc_screen_form_country_placeholder)) },
    leadingIcon = selectedCountry?.let {
      {
        Text(
          text = CountryUtil.getFlag(isoCode = selectedCountry.code),
        )
      }
    },
    trailingIcon = {
      if (selectedCountry != null) {
        Icon(
          modifier = Modifier
            .size(16.dp)
            .noRippleClickable(onClick = onClear),
          tint = MaterialTheme.colorScheme.onBackground,
          painter = painterResource(R.drawable.close),
          contentDescription = null,
        )
      } else {
        ExposedDropdownMenuDefaults.TrailingIcon(expanded = false)
      }
    },
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CountryBottomSheet(
  isExpanded: Boolean,
  onDismiss: () -> Unit,
  state: DiscFormViewModel.State,
  onFilterChanged: (String) -> Unit,
  onSelected: (Country) -> Unit,
) {
  if (!isExpanded) {
    return
  }
  ModalBottomSheet(
    modifier = Modifier
      .statusBarsPadding(),
    onDismissRequest = onDismiss,
    sheetState = rememberModalBottomSheetState(
      skipPartiallyExpanded = true,
    ),
  ) {
    CountryBottomSheetContent(
      state = state,
      onFilterChanged = onFilterChanged,
      onSelected = onSelected,
    )
  }
}

@Composable
private fun CountryBottomSheetContent(
  state: DiscFormViewModel.State,
  onFilterChanged: (String) -> Unit,
  onSelected: (Country) -> Unit,
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxWidth()
      .fillMaxHeight(0.95f)
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    item {
      TextField(
        modifier = Modifier
          .fillMaxWidth(),
        value = state.countryFilterText,
        onValueChange = onFilterChanged,
        label = {
          Text(text = stringResource(R.string.disc_screen_form_country_search_placeholder))
        },
        leadingIcon = {
          Icon(
            modifier = Modifier
              .size(24.dp),
            tint = MaterialTheme.colorScheme.onBackground,
            painter = painterResource(R.drawable.search),
            contentDescription = null,
          )
        },
      )
    }
    items(state.countries) { country ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .noRippleClickable { onSelected(country) },
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = CountryUtil.getFlag(isoCode = country.code),
        )
        Text(
          text = country.name,
          style = MaterialTheme.typography.bodyLarge,
          color = MaterialTheme.colorScheme.onBackground,
        )
      }
    }
  }
}

@LightDarkPreview
@Composable
private fun DiscFormPreview() = PreviewHelper.Screen {
  DiscFormContent(
    modifier = Modifier
      .fillMaxSize(),
    state = DiscFormViewModel.State(),
    onEvent = {},
    onSubmit = {},
  )
}

@LightDarkPreview
@Composable
private fun CountryBottomSheetPreview() = PreviewHelper.Screen {
  CountryBottomSheetContent(
    state = DiscFormViewModel.State(),
    onFilterChanged = {},
    onSelected = {},
  )
}
