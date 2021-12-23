package com.krishiv.myislam.dto;

public class BookListModel {

    String bookName,writerName,haditNumber ,arabicName;

    public String getArabicName() {
        return arabicName;
    }

    public void setArabicName(String arabicName) {
        this.arabicName = arabicName;
    }

    int bookId;

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public BookListModel(String bookName, String writerName, String haditNumber, int bookId, String arabicName) {
        this.bookName = bookName;
        this.writerName = writerName;
        this.haditNumber = haditNumber;
        this.bookId=bookId;
        this.arabicName=arabicName;
    }

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public String getHaditNumber() {
        return haditNumber;
    }

    public void setHaditNumber(String haditNumber) {
        this.haditNumber = haditNumber;
    }
}
