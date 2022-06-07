package data;

import stream.StreamManager;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;

public class Movie implements Comparable<Movie>, Serializable {
    private static final long serialVersionUID = 1L;
    //done
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    //done
    private String name; //Поле не может быть null, Строка не может быть пустой
    //done
    private Coordinates coordinates; //Поле не может быть null
    //done
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    //done
    private Integer oscarsCount; //Значение поля должно быть больше 0, Поле не может быть null
    //done
    private Integer goldenPalmCount; //Значение поля должно быть больше 0, Поле не может быть null
    //done
    private MovieGenre genre; //Поле может быть null
    //done
    private MpaaRating mpaaRating; //Поле может быть null
    //done
    private Person director; //Поле может быть null

    private String user;

    public Movie(String name,Coordinates coordinates,Integer oscarsCount,Integer goldenPalmCount,MovieGenre genre,MpaaRating mpaaRating,Person director,String user){
        this.name=name;
        this.coordinates=coordinates;
        this.oscarsCount=oscarsCount;
        this.goldenPalmCount=goldenPalmCount;
        this.genre=genre;
        this.mpaaRating=mpaaRating;
        this.director=director;
        this.user=user;
    }
    public Movie(){
        this.name=null;
        this.coordinates=null;
        this.oscarsCount=null;
        this.goldenPalmCount=null;
        this.genre=null;
        this.mpaaRating=null;
        this.director=null;
        this.user=null;
    }

    public long getManhattan(){
        return Math.abs(coordinates.getX())+Math.abs(coordinates.getY());
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Integer getGoldenPalmCount() {
        return goldenPalmCount;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public MovieGenre getGenre() {
        return genre;
    }

    public MpaaRating getMpaaRating() {
        return mpaaRating;
    }

    public Person getDirector() {
        return director;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public void setTime(java.time.LocalDateTime creationDate){
        this.creationDate=creationDate;
    }
    public Integer getId(){
        return id;
    }
    public Integer getOscarsCount(){
        return oscarsCount;
    }

    public String getUser() {
        return user;
    }

    @Override
    public int compareTo (Movie o){ return this.id-o.getId();}

    public int compareToByOscarsCount(Movie o){return this.oscarsCount-o.getOscarsCount();}

    public static Movie collectMovie(StreamManager streamManager,String user){
        String movieName = streamManager.stringRequest("Input movie name: ",false);

        Integer xCoordinate = streamManager.intRequest("Input movie X coordinate: ",false,null,293);
        Integer yCoordinate = streamManager.intRequest("Input movie Y coordinate: ",false,null,686);


        Integer movieOscarsCount = streamManager.intRequest("Input movie number of oscars: ",false,1,null);
        Integer goldenPalmCount = streamManager.intRequest("Input movie number of golden palms: ",false,1,null);


        MovieGenre movieGenre = MovieGenre.getEnum(
                streamManager.enumRequest("Input movie genre (ACTION, MUSICAL, THRILLER, HORROR, FANTASY): ",MovieGenre.ACTION,true),true
        );
        MpaaRating mpaaRating= MpaaRating.getEnum(
                streamManager.enumRequest("Input movie MPAA Rating (G, PG, PG_13): ",MpaaRating.G,true),true
        );

        //person
        String personName = streamManager.stringRequest("Input director's name: ",false);
        Integer personHeight = streamManager.intRequest("Input director's height: ",false,1,null);
        Color eyeColor = Color.getEnum(
                streamManager.enumRequest("Input director's color of eyes (GREEN, RED, ORANGE, WHITE): ",Color.GREEN,false),false
        );
        Color hairColor = Color.getEnum(
                streamManager.enumRequest("Input director's color of hair (GREEN, RED, ORANGE, WHITE): ",Color.GREEN,false),false
        );
        Country nationality = Country.getEnum(
                streamManager.enumRequest("Input director's nationality (RUSSIA, USA, CHINA, ITALY, THAILAND): ",Country.USA,true),true
        );

        Integer personX = streamManager.intRequest("Input person X coordinate: ",false,null,null);
        Integer personY = streamManager.intRequest("Input person Y coordinate: ",false,null,null);
        Integer personZ = streamManager.intRequest("Input person Z coordinate: ",false,null,null);

        Movie movie = new Movie(movieName,new Coordinates(xCoordinate,yCoordinate),movieOscarsCount,goldenPalmCount,movieGenre,mpaaRating,new Person(personName,(long)personHeight,eyeColor,hairColor,nationality,new Location((long)personX,personY,personZ)),user);
        movie.setTime(null);

        return movie;
    }

    public static boolean validateMovie(Movie movie,boolean flag){
        try {
            if (flag){
                if (movie.getId() == null) {
                    return false;
                }
                if (movie.getId()<1){
                    return false;
                }
            }
            // getid
            if (movie.getName() == null) {
                return false;
            }
            if (movie.getName().equals("")) {
                return false;
            }
            if (movie.getCoordinates() == null) {
                return false;
            }
            if (movie.getCoordinates().getX() > 293) {
                return false;
            }
            if (movie.getCoordinates().getY() == null) {
                return false;
            }
            if (movie.getCoordinates().getY() > 686) {
                return false;
            }
            if (movie.getCreationDate() == null) {
                return false;
            }
            if (movie.getOscarsCount() == null) {
                return false;
            }
            if (movie.getOscarsCount() < 0) {
                return false;
            }
            if (movie.getGoldenPalmCount() == null) {
                return false;
            }
            if (movie.getGoldenPalmCount() < 0) {
                return false;
            }
            if (movie.getDirector() != null) {
                if (movie.getDirector().getName() == null) {
                    return false;
                }
                if (movie.getDirector().getName().equals("")) {
                    return false;
                }
                if (movie.getDirector().getHeight() == null) {
                    return false;
                }
                if (movie.getDirector().getHeight() < 0) {
                    return false;
                }
                if (movie.getDirector().getEyeColor() == null) {
                    return false;
                }
                if (movie.getDirector().getHairColor() == null) {
                    return false;
                }
                if (movie.getDirector().getLocation() != null) {
                    if (movie.getDirector().getLocation().getX() == null) {
                        return false;
                    }
                }
            }
        }
        catch (Exception e){
            return false;
        }
        return true;
    }


    public String toFormalString() {
        return "Movie{" +
                "id=" + id +
                ", owner= "+user+
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", oscarsCount=" + oscarsCount +
                ", goldenPalmCount=" + goldenPalmCount +
                ", genre=" + genre +
                ", mpaaRating=" + mpaaRating +
                ", director=" + director.toFormalString() +
                '}';
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return  "--------------------------------------------"+"\n"+
                "id               :" + id + "\n" +
                "owner            :" + user + "\n" +
                "name             :" + name + "\n" +
                "coordinates      :" + coordinates + "\n" +
                "creationDate     :" + creationDate.format(formatter) + "\n" +
                "oscarsCount      :" + oscarsCount + "\n" +
                "goldenPalmCount  :" + goldenPalmCount + "\n" +
                "genre            :" + genre + "\n" +
                "mpaaRating       :" + mpaaRating + "\n" +
                "director         :" +"\n"+ director +
                "--------------------------------------------";
    }
}
