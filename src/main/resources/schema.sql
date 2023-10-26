create table CSV(
    ID             int     not null AUTO_INCREMENT,
    SOURCE         varchar,
    CODE_LIST_CODE varchar,
    CODE           varchar not null UNIQUE,
    DISPLAY_VALUE  varchar,
    LONG_DESCRIPTION varchar,
    FROM_DATE DATE,
    TO_DATE DATE,
    SORTING_PRIORITY int,
    PRIMARY KEY (ID)
);