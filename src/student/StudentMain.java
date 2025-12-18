package rs.ac.bg.etf.sab.student;

import rs.ac.bg.etf.sab.operations.*;
import rs.ac.bg.etf.sab.student.*;
import rs.ac.bg.etf.sab.tests.TestHandler;
import rs.ac.bg.etf.sab.tests.TestRunner;


public class StudentMain {
    public static void main(String[] args) throws Exception {

        GeneralOperations generalOperations = new im210261_GeneralOperations();
        GenresOperations genresOperations = new im210261_GenresOperations();
        MoviesOperations moviesOperations = new im210261_MoviesOperations();
        RatingsOperations ratingsOperation = new im210261_RatingsOperations();
        TagsOperations tagsOperations = new im210261_TagsOperations();
        UsersOperations usersOperations = new im210261_UsersOperations();
        WatchlistsOperations watchlistsOperations = new im210261_WatchlistsOperations();

        TestHandler.createInstance(
                genresOperations,
                moviesOperations,
                ratingsOperation,
                tagsOperations,
                usersOperations,
                watchlistsOperations,
                generalOperations);
        TestRunner.runTests();
    }
}