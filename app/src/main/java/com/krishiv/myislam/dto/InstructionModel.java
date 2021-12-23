package com.krishiv.myislam.dto;

public class InstructionModel {
    int instructionId, instructionLanguage;
    String instructionTitle, instructionDescription, instructionImageURL;

    public InstructionModel(String instructionDescription) {
        this.instructionDescription = instructionDescription;
    }

    public int getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(int instructionId) {
        this.instructionId = instructionId;
    }

    public int getInstructionLanguage() {
        return instructionLanguage;
    }

    public void setInstructionLanguage(int instructionLanguage) {
        this.instructionLanguage = instructionLanguage;
    }

    public String getInstructionTitle() {
        return instructionTitle;
    }

    public void setInstructionTitle(String instructionTitle) {
        this.instructionTitle = instructionTitle;
    }

    public String getInstructionDescription() {
        return instructionDescription;
    }

    public void setInstructionDescription(String instructionDescription) {
        this.instructionDescription = instructionDescription;
    }

    public String getInstructionImageURL() {
        return instructionImageURL;
    }

    public void setInstructionImageURL(String instructionImageURL) {
        this.instructionImageURL = instructionImageURL;
    }
}
