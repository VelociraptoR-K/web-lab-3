CREATE TABLE students (
                          id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

                          name VARCHAR(50) NOT NULL,
                          surname VARCHAR(50) NOT NULL,
                          patronymic VARCHAR(50),

                          student_group VARCHAR(5) NOT NULL,
                          isu_id VARCHAR(6) NOT NULL UNIQUE,

                          dormitory_number INT,
                          room INT,
                          move_in_date DATE,

                          foreigner BOOLEAN NOT NULL,
                          notes TEXT,

                          CONSTRAINT students_group_format
                              CHECK (student_group ~ '^[A-Z][0-9]{4}$'),

    CONSTRAINT students_isu_id_format
        CHECK (isu_id ~ '^[1-9][0-9]{5}$'),

    CONSTRAINT students_dormitory_number_range
        CHECK (dormitory_number BETWEEN 1 AND 100),

    CONSTRAINT students_room_range
        CHECK (room BETWEEN 1 AND 1000),

    CONSTRAINT students_move_in_date_min
        CHECK (move_in_date >= DATE '2000-01-01')
);