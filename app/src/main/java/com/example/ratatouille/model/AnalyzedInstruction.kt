package com.example.ratatouille.model

data class AnalyzedInstruction(
    val name: String?,
    val steps: List<InstructionStep>
)

data class InstructionStep(
    val number: Int,
    val step: String,
    val ingredients: List<InstructionItem>?,
    val equipment: List<InstructionItem>?
)

data class InstructionItem(
    val id: Int?,
    val name: String?,
    val localizedName: String?,
    val image: String?
)
