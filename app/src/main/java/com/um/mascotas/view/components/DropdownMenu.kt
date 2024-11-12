import androidx.compose.foundation.layout.Box
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.um.mascotas.model.enums.DisplayableOption

@Composable
fun <T> DropdownMenu(
    label: String,
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T?) -> Unit,
    modifier: Modifier = Modifier
) where T : Enum<T>, T : DisplayableOption {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(modifier = modifier, onClick = { expanded = true }) {
            Text(text = selectedOption?.let { stringResource(id = it.displayNameResId) }
                ?: label)
        }
        androidx.compose.material3.DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            androidx.compose.material3.DropdownMenuItem(
                text = { Text("Todos") },
                onClick = {
                    onOptionSelected(null)
                    expanded = false
                }
            )
            options.forEach { option ->
                androidx.compose.material3.DropdownMenuItem(
                    text = { Text(stringResource(id = option.displayNameResId)) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
