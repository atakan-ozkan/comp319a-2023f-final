package com.example.lifecanvas.screen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.example.lifecanvas.model.NoteModel
import com.example.lifecanvas.viewModel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(noteViewModel: NoteViewModel, navController: NavController) {
    var searchText by remember { mutableStateOf("") }
    var showAddNoteDialog by remember { mutableStateOf(false) }
    var selectedNoteType by remember { mutableStateOf("") }
    var isPublicFilter by remember { mutableStateOf<Boolean?>(null) }
    var noteTypeFilter by remember { mutableStateOf<String?>(null) }
    var createdDateStartFilter by remember { mutableStateOf<Date?>(null) }
    var createdDateEndFilter by remember { mutableStateOf<Date?>(null) }
    var modifiedDateStartFilter by remember { mutableStateOf<Date?>(null) }
    var modifiedDateEndFilter by remember { mutableStateOf<Date?>(null) }
    var noteList by remember { mutableStateOf(noteViewModel.getAllNotes())}

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("My Notes") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("mainScreen") }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
            SearchBar(
                searchText = searchText,
                onSearchTextChanged = { newText ->
                    searchText = newText
                },
                onSearchButtonClicked = {
                    noteList= noteViewModel.searchNotesWithFilters(
                        searchText,
                        isPublicFilter,
                        noteTypeFilter,
                        createdDateStartFilter,
                        createdDateEndFilter,
                        modifiedDateStartFilter,
                        modifiedDateEndFilter
                    )
                }
            )
            ExpandableFilterPanel(
                onResetFilters = {
                searchText = ""
                selectedNoteType= ""
                isPublicFilter = null
                noteTypeFilter= null
                createdDateStartFilter = null
                createdDateEndFilter = null
                modifiedDateStartFilter= null
                modifiedDateEndFilter= null
                noteList = noteViewModel.getAllNotes()
                }
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    PublicPrivateToggleFilter(isPublicFilter) { newValue ->
                        isPublicFilter = newValue
                    }
                    NoteTypeDropdownFilter(noteTypeFilter) { newType ->
                        noteTypeFilter = newType
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    DatePicker("Created After", createdDateStartFilter, Icons.Default.DateRange) { newDate ->
                        createdDateStartFilter = newDate
                    }
                    DatePicker("Created Before", createdDateEndFilter, Icons.Default.DateRange) { newDate ->
                        createdDateEndFilter = newDate
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    DatePicker("Modified After", modifiedDateStartFilter, Icons.Default.DateRange) { newDate ->
                        modifiedDateStartFilter = newDate
                    }
                    DatePicker("Modified Before", modifiedDateEndFilter, Icons.Default.DateRange) { newDate ->
                        modifiedDateEndFilter = newDate
                    }
                }
            }
            NotesList(noteList)
        }
        Column(modifier = Modifier.fillMaxSize()) {
            AddNoteFloatingActionButton { noteType ->
                selectedNoteType = noteType
                showAddNoteDialog = true
            }

            if (showAddNoteDialog) {
                AddNoteDialog(
                    noteType = selectedNoteType,
                    onDismiss = { showAddNoteDialog = false },
                    onNoteAdded = { note ->
                        noteViewModel.insert(note)
                        showAddNoteDialog = false
                        noteList = noteViewModel.getAllNotes()
                    }
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    onSearchButtonClicked: () -> Unit
) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChanged,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        label = { Text("Search Notes") },
        trailingIcon = {
            IconButton(
                onClick = { onSearchButtonClicked() },
                modifier = Modifier.padding(4.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = null)
            }
        }
    )
}
@Composable
fun ExpandableFilterPanel(
    onResetFilters: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Button(onClick = { expanded = !expanded }) {
            Text(if (expanded) "Hide Filters" else "Show Filters")
        }
        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(8.dp), content = content)
        }
        if (expanded) {
            Button(onClick = onResetFilters) {
                Text("Reset")
            }
        }
    }
}


@Composable
fun PublicPrivateToggleFilter(isPublicFilter: Boolean?, onValueChange: (Boolean?) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Private")
        Switch(
            checked = isPublicFilter ?: false,
            onCheckedChange = onValueChange
        )
        Text("Public")
    }
}
@SuppressLint("SimpleDateFormat")
@Composable
fun DatePicker(
    label: String,
    selectedDate: Date?,
    icon: ImageVector,
    onDateSelected: (Date?) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    selectedDate?.let {
        calendar.time = it
    }

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val newDate = Calendar.getInstance()
            newDate.set(year, month, dayOfMonth)
            onDateSelected(newDate.time)
        }, year, month, day
    )

    var formatted =selectedDate?.toString() ?: label
    if(selectedDate!= null){
        val formatter = SimpleDateFormat("yyyy.MM.dd")
        formatted = formatter.format(selectedDate)
    }


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { datePickerDialog.show() }
            .padding(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = formatted
        )
    }
}

@Composable
fun NoteTypeDropdownFilter(noteTypeFilter: String?, onTypeSelected: (String?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val noteTypes = listOf("All", "Text", "Voice", "Image")

    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        Text(
            text = noteTypeFilter ?: "Select Type",
            modifier = Modifier
                .clickable { expanded = true }
                .padding(16.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            noteTypes.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type) },
                    onClick = {
                        expanded = false
                        onTypeSelected(if (type == "All") null else type)
                    }
                )
            }
        }
    }
}

@Composable
fun NotesList(notes: LiveData<List<NoteModel>>) {
    val noteList by notes.observeAsState(initial = emptyList())

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .padding(8.dp),
        contentPadding = PaddingValues(8.dp),
        content = {
            items(noteList.size) { index ->
                NoteItem(noteList[index])
            }
        }
    )
}


@Composable
fun NoteItem(note: NoteModel) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = note.title, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Type: ${note.type}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteDialog(
    noteType: String,
    onDismiss: () -> Unit,
    onNoteAdded: (NoteModel) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var isPrivate by remember { mutableStateOf(false) }
    val currentDateTime = remember { Date() }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Add New $noteType Note") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") }
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isPrivate,
                        onCheckedChange = { isPrivate = it }
                    )
                    Text("Set Private Mode")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onNoteAdded(
                        NoteModel(
                            type = noteType,
                            title = title,
                            content = "",
                            isPublic = !isPrivate,
                            filePath = null,
                            createdDate = currentDateTime,
                            modifiedDate = currentDateTime
                        )
                    )
                    onDismiss()
                }
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AddNoteFloatingActionButton(onAddNoteTypeSelected: (String) -> Unit) {
    var showMenu by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .zIndex(1f), contentAlignment = Alignment.BottomEnd
    ) {
        // Custom FAB
        FloatingActionButton(
            onClick = { showMenu = !showMenu },
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Note")
        }
        if (showMenu) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 72.dp)
            ) {
                NoteTypeButton("Text") { onAddNoteTypeSelected("Text"); showMenu = false }
                NoteTypeButton("Voice") { onAddNoteTypeSelected("Voice"); showMenu = false }
                NoteTypeButton("Image") { onAddNoteTypeSelected("Image"); showMenu = false }
            }
        }
    }
}

@Composable
fun NoteTypeButton(type: String, onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        contentColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .padding(8.dp)
            .zIndex(1f)
    ) {
        Text(type, style = MaterialTheme.typography.labelMedium)
    }
}


