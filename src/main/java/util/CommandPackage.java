package util;

import data.Movie;

import java.io.Serializable;

public class CommandPackage implements Serializable {

    private String command;
    private Movie movie;
    private Integer number;
    private String string;
    private TypeOfArgumentEnum type;
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCommand() {
        return command;
    }

    public Movie getMovie() {
        return movie;
    }

    public Integer getNumber() {
        return number;
    }

    public String getString() {
        return string;
    }

    public TypeOfArgumentEnum getType() {
        return type;
    }

    public CommandPackage(String command, Movie movie){
        this.command=command;
        this.movie=movie;
        number=null;
        string=null;
        type=TypeOfArgumentEnum.MOVIE;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public CommandPackage(String command, Integer number, Movie movie){
        this.command=command;
        this.movie=movie;
        this.number=number;
        string=null;
        type=TypeOfArgumentEnum.INTEGER_MOVIE;
    }
    public CommandPackage(String command,Integer number){
        this.command=command;
        this.number=number;
        movie=null;
        string=null;
        type=TypeOfArgumentEnum.INTEGER;
    }
    public CommandPackage(String command,String string){
        this.command=command;
        this.string=string;
        type=TypeOfArgumentEnum.STRING;
    }
    public CommandPackage(String command){
        this.command=command;
        type=TypeOfArgumentEnum.NULL;
    }

    @Override
    public String toString() {
        String mov;
        if (movie==null){
            mov="null";
        }
        else mov = movie.toFormalString();
        return "CommandPackage{" +
                "command='" + command + '\'' +
                ", movie=" + mov  +
                ", number=" + number +
                ", string='" + string + '\'' +
                ", type=" + type +
                ", username=" +username+
                ", password=" + password+
                '}';
    }
}
