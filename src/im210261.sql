IF EXISTS (SELECT name FROM sys.databases WHERE name = N'filmovi')
BEGIN
    ALTER DATABASE filmovi SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE filmovi;
END
GO

-- Create new database
CREATE DATABASE filmovi;
GO

-- Use the database
USE filmovi;
GO

PRINT 'Database "filmovi" created and selected.';
GO


CREATE TABLE Users (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Username NVARCHAR(100) NOT NULL UNIQUE,
    Rewards INT NOT NULL DEFAULT 0
);
GO

CREATE TABLE Genres (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Name NVARCHAR(100) NOT NULL UNIQUE
);
GO

CREATE TABLE Movies (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Title NVARCHAR(100) NOT NULL,
    Director NVARCHAR(100) NOT NULL
);
GO

CREATE TABLE MovieGenres (
    MovieId INT NOT NULL,
    GenreId INT NOT NULL,
    PRIMARY KEY (MovieId, GenreId),
    CONSTRAINT FK_MovieGenres_Movie FOREIGN KEY (MovieId) REFERENCES Movies(Id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT FK_MovieGenres_Genre FOREIGN KEY (GenreId) REFERENCES Genres(Id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION
);
GO

CREATE TABLE MovieTags (
    MovieId INT NOT NULL,
    TagName NVARCHAR(100) NOT NULL,
    PRIMARY KEY (MovieId, TagName),
    CONSTRAINT FK_MovieTags_Movie FOREIGN KEY (MovieId) REFERENCES Movies(Id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
GO

CREATE TABLE Watchlists (
    UserId INT NOT NULL,
    MovieId INT NOT NULL,
    PRIMARY KEY (UserId, MovieId),
    CONSTRAINT FK_Watchlists_User FOREIGN KEY (UserId) REFERENCES Users(Id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
    CONSTRAINT FK_Watchlists_Movie FOREIGN KEY (MovieId) REFERENCES Movies(Id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
GO

CREATE TABLE Ratings (
    UserId INT NOT NULL,
    MovieId INT NOT NULL,
    Score INT NOT NULL CHECK (Score >= 1 AND Score <= 10),
    PRIMARY KEY (UserId, MovieId),
    CONSTRAINT FK_Ratings_User FOREIGN KEY (UserId) REFERENCES Users(Id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
    CONSTRAINT FK_Ratings_Movie FOREIGN KEY (MovieId) REFERENCES Movies(Id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
GO