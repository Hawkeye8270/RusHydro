package com.example.service;

public class ElementXPath {

    public static final String SELECT_RIVER = "//div[normalize-space()='Река']/ancestor::div[contains(@class, 'select-with-name')]//div[contains(@class, 'trigger-arrow')]";
    public static final String SELECT_RIVER_VOLGA = "//li[text()='Волга']";
    //    public static final String SELECT_GES = "//select[@name='ges' and @id='ges' and @class='select-with-name__select']";
    public static final String SELECT_GES = "//div[normalize-space()='ГЭС']/ancestor::div[contains(@class, 'select-with-name')]//div[contains(@class, 'trigger-arrow')]";
    public static final String SELECT_GES_RYBINSKAYA = "//li[text()='Рыбинская']";

    public static final String SELECT_DATE = "//input[@id='water-date' and @name='water-date-input']";
    public static final String SELECT_DATE_15 = "//div[contains(@class, 'datepicker--cell-day') and @data-date='15']";

    public static final String INPUT_DATE = "01.05.2025";
    public static final String SELECT_PERIOD_OF_DAYS = "//div[@class='water-day__trigger' and normalize-space()='30']";

    public static final String ALL_RIVER_ALTERNATIVE = "//div[@data-river='Все реки']";
    public static final String JR_TEST = "//a[@class='tabs__button' and normalize-space()='Мое обучение']";
    public static final String CLOSE_COOKIE = "//div[@class='cookie-files-close']";

}
