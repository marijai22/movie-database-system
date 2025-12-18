CREATE TRIGGER TR_BLOCK_EXTREME_im210261
ON Ratings
AFTER INSERT, UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @UserId INT, @MovieId INT, @NewScore INT, @OldScore INT;

    DECLARE cur CURSOR FOR
    SELECT i.UserId, i.MovieId, i.Score
    FROM inserted i
    WHERE i.Score IN (1, 10);

    OPEN cur;
    FETCH NEXT FROM cur INTO @UserId, @MovieId, @NewScore;

    WHILE @@FETCH_STATUS = 0
    BEGIN
        SET @OldScore = NULL;

        IF EXISTS (SELECT 1 FROM deleted WHERE UserId = @UserId AND MovieId = @MovieId)
        BEGIN
            SELECT @OldScore = Score
            FROM deleted
            WHERE UserId = @UserId AND MovieId = @MovieId;
        END

        IF @OldScore IS NOT NULL AND @OldScore IN (1, 10) AND @NewScore NOT IN (1, 10)
        BEGIN
            FETCH NEXT FROM cur INTO @UserId, @MovieId, @NewScore;
            CONTINUE;
        END

        DECLARE @ExtremeCount INT = 0;
        DECLARE @NeutralCount INT = 0;

        SELECT @ExtremeCount = COUNT(DISTINCT r.MovieId)
        FROM Ratings r
        JOIN MovieGenres mg1 ON r.MovieId = mg1.MovieId
        WHERE r.UserId = @UserId
          AND r.Score IN (1, 10)
          AND EXISTS (
              SELECT 1
              FROM MovieGenres mg2
              WHERE mg2.MovieId = @MovieId
                AND mg2.GenreId = mg1.GenreId
          );

        SELECT @NeutralCount = COUNT(DISTINCT r.MovieId)
        FROM Ratings r
        JOIN MovieGenres mg1 ON r.MovieId = mg1.MovieId
        WHERE r.UserId = @UserId
          AND r.Score IN (6, 7, 8)
          AND EXISTS (
              SELECT 1
              FROM MovieGenres mg2
              WHERE mg2.MovieId = @MovieId
                AND mg2.GenreId = mg1.GenreId
          );

        IF @ExtremeCount >4 AND @NeutralCount < 3
        BEGIN
            RAISERROR('Blocked extreme rating (1 or 10) for user in this genre.', 16, 1);
            ROLLBACK TRANSACTION;
            CLOSE cur;
            DEALLOCATE cur;
            RETURN;
        END

        FETCH NEXT FROM cur INTO @UserId, @MovieId, @NewScore;
    END

    CLOSE cur;
    DEALLOCATE cur;
END;
GO



CREATE PROCEDURE SP_REWARD_USER_im210261
    @UserId INT,
    @MovieId INT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @TotalRatings INT;
    SELECT @TotalRatings = COUNT(*) FROM Ratings WHERE UserId = @UserId;

    IF @TotalRatings < 10
    BEGIN
        RETURN;
    END

    DECLARE @IsInFavoriteGenre BIT = 0;

    IF EXISTS (
        SELECT 1
        FROM MovieGenres mg
        WHERE mg.MovieId = @MovieId
          AND EXISTS (
              SELECT 1
              FROM Ratings r
              JOIN MovieGenres mg2 ON r.MovieId = mg2.MovieId
              WHERE r.UserId = @UserId
                AND mg2.GenreId = mg.GenreId
              GROUP BY mg2.GenreId
              HAVING AVG(CAST(r.Score AS DECIMAL(10,3))) >= 8.0
          )
    )
    BEGIN
        SET @IsInFavoriteGenre = 1;
    END

    IF @IsInFavoriteGenre = 0
    BEGIN
        RETURN;
    END

    DECLARE @GlobalAvg DECIMAL(10,3);

    SELECT @GlobalAvg = AVG(CAST(r.Score AS DECIMAL(10,3)))
    FROM Ratings r
    WHERE r.MovieId = @MovieId
      AND r.UserId <> @UserId;

    IF @GlobalAvg IS NOT NULL AND @GlobalAvg < 6.0
    BEGIN
        UPDATE Users
        SET Rewards = Rewards + 1
        WHERE Id = @UserId;
    END
END;
GO