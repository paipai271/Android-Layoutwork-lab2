package com.example.test2_tasklist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** 任务模型：done 用可变状态包裹，勾选时界面自动重组 */
class Task(val title: String) {
    var done by mutableStateOf(false)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                TaskListApp()
            }
        }
    }
}

@Composable
fun TaskListApp() {
    // 初始状态：3 项任务，第 1 项已完成
    val tasks = remember {
        mutableStateListOf(
            Task("学习Column和Row").also { it.done = true },
            Task("学习状态管理"),
            Task("完成Compose实验")
        )
    }
    var input by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 标题
        Text(
            text = "课程学习任务",
            color = Color.Red,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 输入框 + 添加按钮
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                placeholder = { Text("请输入学习任务") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    val name = input.trim()
                    if (name.isNotEmpty()) {
                        tasks.add(Task(name))
                        input = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("添加")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 完成进度：已完成：X/Y（实时计算）
        Text(
            text = "已完成：${tasks.count { it.done }}/${tasks.size}",
            color = Color.Red,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 任务列表
        LazyColumn {
            items(tasks) { task ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 勾选框：已完成 √ / 未完成 □，点击切换状态
                    Text(
                        text = if (task.done) "√" else "□",
                        color = if (task.done) Color.Red else Color.Black,
                        fontSize = 20.sp,
                        modifier = Modifier.clickable { task.done = !task.done }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // 任务文字：完成后加删除线
                    Text(
                        text = task.title,
                        fontSize = 18.sp,
                        textDecoration = if (task.done) {
                            TextDecoration.LineThrough
                        } else {
                            TextDecoration.None
                        },
                        modifier = Modifier.weight(1f)
                    )
                    // 删除按钮
                    TextButton(onClick = { tasks.remove(task) }) {
                        Text("删除", color = Color.Red)
                    }
                }
            }
        }
    }
}