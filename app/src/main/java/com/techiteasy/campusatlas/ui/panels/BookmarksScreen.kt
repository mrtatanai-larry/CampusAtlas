package com.techiteasy.campusatlas.ui.panels

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DriveFileRenameOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techiteasy.campusatlas.R
import com.techiteasy.campusatlas.data.AppDatabase
import com.techiteasy.campusatlas.data.BookmarkList
import com.techiteasy.campusatlas.ui.theme.CampusAtlasTheme
import kotlinx.coroutines.launch

@Composable
fun BookmarksScreen() {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val dao = database.bookmarkDao()
    val scope = rememberCoroutineScope()
    
    val bookmarkLists by dao.getAllLists().collectAsState(initial = emptyList())
    
    var showAddDialog by remember { mutableStateOf(false) }
    var newListName by remember { mutableStateOf("") }

    var listToEdit by remember { mutableStateOf<BookmarkList?>(null) }
    var editListName by remember { mutableStateOf("") }
    
    var listToDelete by remember { mutableStateOf<BookmarkList?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Clean Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Collections",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            FilledTonalButton(
                onClick = { showAddDialog = true },
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_24px),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("New list", fontWeight = FontWeight.Medium)
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                BookmarkItem(
                    name = "Favorites",
                    count = 0,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = Color(0xFFFFDAD6),
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    containerColor = Color(0xFFE53935),
                    isSystemList = true
                )
            }

            items(bookmarkLists) { list ->
                if (list.name != "Favorites") {
                    BookmarkItem(
                        name = list.name,
                        count = 0,
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.bookmark_24px),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                        onEditClick = {
                            listToEdit = list
                            editListName = list.name
                        },
                        onDeleteClick = {
                            listToDelete = list
                        }
                    )
                }
            }
        }
    }

    // Add dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Create new list") },
            text = {
                OutlinedTextField(
                    value = newListName,
                    onValueChange = { newListName = it },
                    label = { Text("List name") },
                    placeholder = { Text("e.g. Study Spots") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newListName.isNotBlank()) {
                            scope.launch {
                                dao.insertList(BookmarkList(name = newListName))
                                newListName = ""
                                showAddDialog = false
                            }
                        }
                    },
                    enabled = newListName.isNotBlank()
                ) {
                    Text("Create")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Edit dialog
    listToEdit?.let { list ->
        AlertDialog(
            onDismissRequest = { listToEdit = null },
            title = { Text("Rename list") },
            text = {
                OutlinedTextField(
                    value = editListName,
                    onValueChange = { editListName = it },
                    label = { Text("List name") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editListName.isNotBlank()) {
                            scope.launch {
                                dao.updateList(list.copy(name = editListName))
                                listToEdit = null
                            }
                        }
                    },
                    enabled = editListName.isNotBlank()
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { listToEdit = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Delete confirmation
    listToDelete?.let { list ->
        AlertDialog(
            onDismissRequest = { listToDelete = null },
            title = { Text("Delete list?") },
            text = { Text("Are you sure you want to delete '${list.name}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            dao.deleteList(list)
                            listToDelete = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { listToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun BookmarkItem(
    name: String,
    count: Int,
    icon: @Composable () -> Unit,
    containerColor: Color,
    isSystemList: Boolean = false,
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    ListItem(
        headlineContent = {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        },
        supportingContent = {
            Text(
                text = "$count places",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(containerColor),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
        },
        trailingContent = {
            if (!isSystemList) {
                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Rename") },
                            onClick = {
                                expanded = false
                                onEditClick()
                            },
                            leadingIcon = { 
                                Icon(
                                    imageVector = Icons.Outlined.DriveFileRenameOutline, 
                                    contentDescription = null, 
                                    modifier = Modifier.size(20.dp)
                                ) 
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete", color = MaterialTheme.colorScheme.error) },
                            onClick = {
                                expanded = false
                                onDeleteClick()
                            },
                            leadingIcon = { 
                                Icon(
                                    imageVector = Icons.Outlined.Delete, 
                                    contentDescription = null, 
                                    tint = MaterialTheme.colorScheme.error, 
                                    modifier = Modifier.size(20.dp)
                                ) 
                            }
                        )
                    }
                }
            }
        },
        modifier = Modifier
            .clickable { /* Navigate to details */ }
            .padding(horizontal = 4.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun BookmarksScreenPreview() {
    CampusAtlasTheme {
        Surface {
            BookmarksScreen()
        }
    }
}
