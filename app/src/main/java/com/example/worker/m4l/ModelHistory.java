package com.example.worker.m4l;

public class ModelHistory {
    private int id;
    private int move;
    private int manual;
    private int active;
    private String date;
    public ModelHistory()
    {
        id = 0;
        move = 0;
        manual = 0;
        active = 0;
        date = "2000-1-1";
    }
    public ModelHistory(int _id, int _move, int _manual, int _active, String _date)
    {
        id = _id;
        move = _move;
        manual = _manual;
        active = _active;
        date = _date;
    }
    public void setModelHistory(int _id, int _move, int _manual, int _active, String _date)
    {
        id = _id;
        move = _move;
        manual = _manual;
        active = _active;
        date = _date;
    }
    public int getId(){
        return id;
    }
    public int getMove(){
        return move;
    }
    public int getManual(){
        return manual;
    }
    public int getActive(){
        return active;
    }
    public String getDate(){
        return date;
    }
    public void setId(int _id){
        id = _id;
    }
    public void setMove(int arg){
        move = arg;
    }
    public void setManual(int arg){
        manual = arg;
    }
    public void setActive(int arg){
        active = arg;
    }
    public void setDate(String arg){
        date = arg;
    }
}
