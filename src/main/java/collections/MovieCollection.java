package collections;

import data.*;
import database.DBAnswer;
import database.DBManager;
import stream.OutputColor;
import util.CommandPackage;
import util.Result;
import util.ServerResponse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.stream.Collectors;


public class MovieCollection {
    TreeSet<Movie> collection;
    java.time.LocalDateTime collectionCreationDate;
    DBManager dbManager;

    private Integer lastId;

    public MovieCollection(DBManager dbManager){
        collection =new TreeSet<Movie>();
        collectionCreationDate=java.time.LocalDateTime.now();
        lastId=0;
        this.dbManager=dbManager;
    }

    public TreeSet<Movie> getCollection() {
        return collection;
    }

    public ServerResponse add(CommandPackage arg) {

        try {
            if (!dbManager.loginUser(arg.getUsername(), arg.getPassword())){
                return new ServerResponse("Something went wrong with the authentification",OutputColor.RED,Result.FAILURE);
            }
        } catch (Exception e){
            return new ServerResponse("Something went wrong with the authentification",OutputColor.RED,Result.FAILURE);
        }

        if (collection.stream().filter(e->e.getUser().equals(arg.getUsername())).count() >=100){
            return new ServerResponse("You have reached limit of movies per one user. Pay or leave",OutputColor.RED,Result.FAILURE);
        }

        Movie temp = arg.getMovie();
        temp.setTime(LocalDateTime.now());

        if (Movie.validateMovie(temp,false)) {
//            System.out.println(temp.toFormalString());

            Integer id= dbManager.addMovie(temp);
            System.out.println(id);

            if (id!=0 && id!=null) {
                temp.setId(id);
//                lastId += 1;
                collection.add(temp);
                return new ServerResponse("Element was successfully added", OutputColor.GREEN,Result.SUCCESS);
            }
            else {
                return new ServerResponse("Incorrect movie id",OutputColor.RED,Result.FAILURE);
            }
        }
        else{
            return new ServerResponse("Something went wrong...",OutputColor.RED,Result.FAILURE);
        }
    }
    public boolean setCollection(){
        try{
            ResultSet data = dbManager.getCollection();

            while(data.next()){
                String sGenre = data.getString(9);
                MovieGenre genre;
                if (sGenre==null){
                    genre=null;
                }
                else{
                    genre=MovieGenre.valueOf(sGenre);
                }
                String sRating = data.getString(10);
                MpaaRating rating;
                if (sRating==null){
                    rating=null;
                }
                else{
                    rating=MpaaRating.valueOf(sRating);
                }
                String sCountry = data.getString(15);
                Country country;
                if (sCountry==null){
                    country=null;
                }
                else{
                    country=Country.valueOf(sCountry);
                }
                Movie movie = new Movie(
//                        data.getInt(1),
                        data.getString(2),
                        new Coordinates(data.getInt(3),(Integer)data.getInt(4)),
                        data.getInt(7),
                        data.getInt(8),
                        genre,
                        rating,
                        new Person(
                                data.getString(11),
                                data.getLong(12),
                                Color.valueOf(data.getString(13)),
                                Color.valueOf(data.getString(14)),
                                country,
                                new Location(
                                        data.getLong(16),
                                        data.getFloat(17),
                                        data.getLong(18)
                                )
                        ),
                        data.getString(19)
                );
                movie.setId(data.getInt(1));
                movie.setTime(LocalDateTime.of(data.getDate(5).toLocalDate(), data.getTime(6).toLocalTime()));
                collection.add(movie);
            }
            return true;
        }catch (SQLException | NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }

    public ServerResponse addIfMax(CommandPackage arg){
        try {
            if (!dbManager.loginUser(arg.getUsername(), arg.getPassword())){
                return new ServerResponse("Something went wrong with the authentification",OutputColor.RED,Result.FAILURE);
            }
        } catch (Exception e){
            return new ServerResponse("Something went wrong with the authentification",OutputColor.RED,Result.FAILURE);
        }
        Movie temp = arg.getMovie();
        temp.setTime(LocalDateTime.now());
        if (Movie.validateMovie(temp,false)) {
            boolean flag = collection.stream().filter(e-> e.getUser().equals(arg.getUsername())).anyMatch(movie -> movie.compareToByOscarsCount(temp) > 0);

            Integer id= dbManager.addMovie(temp);

            if (id!=0 && id!=null) {
                temp.setId(id);
                if (flag) {
                    return new ServerResponse("Element wasn't added", OutputColor.GREEN,Result.SUCCESS);
                } else {
                    collection.add(temp);
                    return new ServerResponse("Element was successfully added", OutputColor.GREEN,Result.SUCCESS);
                }
            }
            else {
                return new ServerResponse("Something went wrong with adding element",OutputColor.RED,Result.FAILURE);
            }


        }
        else{
            return new ServerResponse("Something went wrong...",OutputColor.RED,Result.FAILURE);
        }
    }

    public ServerResponse removeLower(CommandPackage arg){
        try {
            if (!dbManager.loginUser(arg.getUsername(), arg.getPassword())){
                return new ServerResponse("Something went wrong with the authentification",OutputColor.RED,Result.FAILURE);
            }
        } catch (Exception e){
            return new ServerResponse("Something went wrong with the authentification",OutputColor.RED,Result.FAILURE);
        }

        Movie temp = arg.getMovie();
        temp.setTime(LocalDateTime.now());
        if (Movie.validateMovie(temp,false)) {
            collection.stream().filter(e-> e.getUser().equals(arg.getUsername())).filter(e -> e.compareToByOscarsCount(temp) < 0).forEach(e->dbManager.removeById(e.getId(), e.getUser()));
            collection = collection.stream().filter(e-> !e.getUser().equals(arg.getUsername())||(e.getUser().equals(arg.getUsername()) && e.compareToByOscarsCount(temp) >= 0)).collect(Collectors.toCollection(TreeSet::new));

            return new ServerResponse("Elements were successfully deleted", OutputColor.GREEN,Result.SUCCESS);
        }
        else{
            return new ServerResponse("Something went wrong...",OutputColor.RED,Result.FAILURE);
        }
    }

    public ServerResponse minByOscarsCount(CommandPackage arg){
        try {
            if (!dbManager.loginUser(arg.getUsername(), arg.getPassword())){
                return new ServerResponse("Something went wrong with the authentification",OutputColor.RED,Result.FAILURE);
            }
        } catch (Exception e){
            return new ServerResponse("Something went wrong with the authentification",OutputColor.RED,Result.FAILURE);
        }

        if (collection.isEmpty()){
            return new ServerResponse("Collection is empty",OutputColor.BLUE,Result.SUCCESS);
        }
        return new ServerResponse(collection.stream().filter(e-> e.getUser().equals(arg.getUsername())).min(Movie::compareToByOscarsCount).toString(),OutputColor.BLUE,Result.SUCCESS);
    }

    public ServerResponse update(CommandPackage arg){
        try {
            if (!dbManager.loginUser(arg.getUsername(), arg.getPassword())){
                return new ServerResponse("Something went wrong with the authentification",OutputColor.RED,Result.FAILURE);
            }
        } catch (Exception e){
            return new ServerResponse("Something went wrong with the authentification",OutputColor.RED,Result.FAILURE);
        }
        try{
            System.out.println(arg.getNumber());
            Integer id=Integer.valueOf(arg.getNumber());
            Boolean flag =delete(id);
            if (flag==Boolean.TRUE){
                Movie temp =arg.getMovie();
                temp.setTime(LocalDateTime.now());
                temp.setId(id);
                if (dbManager.updateById(temp,id) == DBAnswer.SUCCESSFUL ){
                    collection.add(temp);
                    return new ServerResponse("Element was successfully updated",OutputColor.GREEN,Result.SUCCESS);
                    }
                else {
                    return new ServerResponse("Something went wrong...", OutputColor.RED, Result.FAILURE);
                }
            }
            else if(flag==Boolean.FALSE){
                return new ServerResponse("No such ID in collection",OutputColor.RED,Result.FAILURE);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new ServerResponse("Argument is not valid",OutputColor.RED,Result.FAILURE);
        }
        return null;
    }

    public ServerResponse remove(CommandPackage arg){
        try {
            if (!dbManager.loginUser(arg.getUsername(), arg.getPassword())){
                return new ServerResponse("Something went wrong with the authentification",OutputColor.RED,Result.FAILURE);
            }
        } catch (Exception e){
            return new ServerResponse("Something went wrong with the authentification",OutputColor.RED,Result.FAILURE);
        }
        try{
            Integer id=Integer.valueOf(arg.getNumber());
            Boolean flag = dbManager.removeById(id, arg.getUsername())==DBAnswer.SUCCESSFUL && delete(id);
            if (flag==Boolean.TRUE){
                return new ServerResponse("Element was successfully deleted",OutputColor.GREEN,Result.SUCCESS);
            }
            else if(flag==Boolean.FALSE) {
                return new ServerResponse("No such ID in collection",OutputColor.RED,Result.FAILURE);
            }
            else {
                return new ServerResponse("Collection is empty",OutputColor.GREEN,Result.SUCCESS);
            }
        }
        catch (Exception e){
            return new ServerResponse("Argument is not an integer",OutputColor.RED,Result.FAILURE);
        }
    }
    public ServerResponse countGreater(CommandPackage arg){
        try {
            if (!dbManager.loginUser(arg.getUsername(), arg.getPassword())){
                return new ServerResponse("Something went wrong with the authentification",OutputColor.RED,Result.FAILURE);
            }
        } catch (Exception e){
            return new ServerResponse("Something went wrong with the authentification",OutputColor.RED,Result.FAILURE);
        }
        return new ServerResponse(
                    String.valueOf(
                            collection.stream().filter(e-> e.getUser().equals(arg.getUsername())).filter(movie -> movie.getOscarsCount() >  arg.getNumber()).count()
                    ),
                OutputColor.BLUE,Result.SUCCESS
            );
    }
    public ServerResponse printAscending(CommandPackage arg){
        return print(arg);
    }

    public Boolean delete(Integer id){
        if (collection.isEmpty())  {
            return null;
        }

        for (Movie movie: collection){
            if (movie.getId()==id){
                collection.remove(movie);
                return true;
            }
        }

        return false;
    }

    public ServerResponse clear(CommandPackage arg){
        try {
            if (!dbManager.loginUser(arg.getUsername(), arg.getPassword())){
                return new ServerResponse("Something went wrong with the authentification",OutputColor.RED,Result.FAILURE);
            }
        } catch (Exception e){
            return new ServerResponse("Something went wrong with the authentification",OutputColor.RED,Result.FAILURE);
        }
        if (dbManager.clear(arg.getUsername())==DBAnswer.SUCCESSFUL) {
            collection= collection.stream().filter(e->!e.getUser().equals(arg.getUsername())).collect(Collectors.toCollection(TreeSet::new));
//            setCollection();
            return new ServerResponse("Collection was cleared successfully", OutputColor.GREEN, Result.SUCCESS);
        }
        else return new ServerResponse("Collection wasn't cleared", OutputColor.RED, Result.FAILURE);
    }

    public ServerResponse print(CommandPackage arg){
        try {
            if (!dbManager.loginUser(arg.getUsername(), arg.getPassword())){
                return new ServerResponse("Something went wrong with the authentification",OutputColor.RED,Result.FAILURE);
            }
        } catch (Exception e){
            return new ServerResponse("Something went wrong with the authentification",OutputColor.RED,Result.FAILURE);
        }
        String line="";
        if (collection.isEmpty()){
            return new ServerResponse("Collection is empty",OutputColor.BLUE,Result.SUCCESS);
        }
        else {
            for (Movie movie: collection.stream().sorted(new Comparator<Movie>() {
                @Override
                public int compare(Movie o1, Movie o2) {
                    return (int)(o1.getManhattan()-o2.getManhattan());
                }
            }).collect(Collectors.toList())) line+=movie.toString()+"\n";
        }
        if (line.equals("")) return new ServerResponse("Collection is empty",OutputColor.BLUE,Result.SUCCESS);
        return new ServerResponse(line,OutputColor.BLUE,Result.SUCCESS);
    }

    public ServerResponse help(){
        return new ServerResponse(
                    "help                                           : just help" + "\n"+
                        "info                                           : information about collection" + "\n"+
                        "show                                           : show all elements of collection" +"\n"+
                        "add {element}                                  : add element to collection" +"\n"+
                        "update id {element}                            : update element by id" +"\n"+
                        "remove_by_id id                                : remove element by id" +"\n"+
                        "clear                                          : clear collection" +"\n"+
                        "save                                           : save collection to the file" + "\n"+
                        "execute_script file_name                       : execute script" +"\n"+
                        "exit                                           : exit from application" + "\n"+
                        "add_if_max {element}                           : add element if it has max oscars count" + "\n"+
                        "remove_lower {element}                         : remove all elements that lower than inputed element" + "\n"+
                        "history                                        : print last 13 commands" + "\n"+
                        "min_by_oscars_count                            : print element that has minimum of oscars count" + "\n"+
                        "count_greater_than_oscars_count oscarsCount    : print the amount of elements that has oscars more than oscarsCount" + "\n"+
                        "print_ascending                                : print elements in ascending order",OutputColor.BLUE,Result.SUCCESS);
    }
    public ServerResponse info(){
        return new ServerResponse(
                    "Collection type     : TreeSet"+"\n"+
                        "Amount of elements  : " + collection.size()+"\n"+
                        "Date of creation    : " + collectionCreationDate,OutputColor.BLUE, Result.SUCCESS);
    }

}