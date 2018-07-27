package com.elegantappstore.mynotes.Models


class Note {
    var id:Int?=null
    var noteTitle:String?=null
    var noteDescription:String?=null
    var noteDate:String?=null

    constructor(id:Int,noteTitle:String, noteDescription:String, noteDate:String){

        this.id=id
        this.noteTitle=noteTitle
        this.noteDescription=noteDescription
        this.noteDate=noteDate
    }
}