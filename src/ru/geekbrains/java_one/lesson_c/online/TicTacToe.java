package ru.geekbrains.java_one.lesson_c.online;

import java.util.Random;
import java.util.Scanner;

public class TicTacToe {

    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = 'O';
    private static final char DOT_EMPTY = '_';
    //
    private static int lenght_win;
    //

    private static int fieldSizeX;
    private static int fieldSizeY;
    private static char[][] field;

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random RANDOM = new Random();

    private static void initField() {
        fieldSizeY = 5; // 5; //3;
        fieldSizeX = 5; // 5; //3;
        lenght_win = fieldSizeX-1;
        field = new char[fieldSizeY][fieldSizeX];
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                field[y][x] = DOT_EMPTY;
            }
        }
    }

    private static void printField() {
        System.out.println("------");
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                System.out.print(field[y][x] + "|");
            }
            System.out.println();
        }
    }

    private static boolean isValidCell(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >=0 && y < fieldSizeY;
    }

    private static boolean isEmptyCell(int x, int y) {
        return field[y][x] == DOT_EMPTY;
    }

    private static void humanTurn() {
        int x;
        int y;
        do {
            System.out.println("Введите координаты поля (1-3) >>> ");
            x = SCANNER.nextInt() - 1;
            y = SCANNER.nextInt() - 1;
        } while (!isValidCell(x, y) || !isEmptyCell(x, y));
        field[y][x] = DOT_HUMAN;
    }

    private static void aiTurn() {
        int x;
        int y;
        do {
            x = RANDOM.nextInt(fieldSizeX);
            y = RANDOM.nextInt(fieldSizeY);
        } while (!isEmptyCell(x, y));
        field[y][x] = DOT_AI;

    }

    private static boolean isFieldFull() {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (field[y][x] == DOT_EMPTY) return false;
            }
        }
        return true;
    }

    /**
     * Метод поиска победителя циклами.
     * @param c - значение для поиска
     * @return ture/false
     */
    private static boolean checkWin(char c) {

        boolean diagonal_1 = true;
        boolean diagonal_2 = true;
        boolean column = true;
        boolean row = true;

        //Происходит поиск по диагоналям. Если последовательность продолжается, то переменная =true, как только встречается значение отличное от С, =false.
        for(int i = 0; i<field.length; i++){
            diagonal_1 = (diagonal_1 && field[i][i] == c);
            diagonal_2 = (diagonal_2 && field[i][field.length-i-1] == c);
        }

        //если по диагонали нашли победу, то выходим, нет смысла искать далее.
        if (diagonal_1 || diagonal_2) return true;

        //происходит поиск по строкам и столбца. Если последовательность продолжается, то переменная true, как только встречается значение отличное от С,false.
        for(int i = 0; i < field.length; i++){
            column = true;
            row = true;
            for (int j = 0; j < field.length; j++) {
                column = (column && field[i][j] == c);
                row = (row && field[j][i] == c);
            }
            if(column || row) return true;
        }

        return false;
    }

    /**
     * Метод поиска победите с заданной длиной последовательных значений. Метод checkWin(char c) ищет на всей ширене поля. Разница в том, что ограничил поле для поиска заданной длиной.
     * @param c - значение для поиска
     * @param X1 - начало массива по оси X
     * @param Y1 - начало массива по оси Y
     * @return
     */
    private static boolean checkWin(char c, int X1, int Y1) {

        boolean diagonal_1 = true;
        boolean diagonal_2 = true;
        boolean column = true;
        boolean row = true;


         for(int i = X1; i<X1+lenght_win; i++){
            diagonal_1 = (diagonal_1 && field[i][i] == c);
            diagonal_2 = (diagonal_2 && field[i][(X1+lenght_win)-i-1] == c);
        }

        if (diagonal_1 || diagonal_2) return true;

        for(int i = X1; i <X1+lenght_win; i++){
            column = true;
            row = true;
            for (int j = Y1; j <Y1+lenght_win; j++) {
                column = (column && field[i][j] == c);
                row = (row && field[j][i] == c);
            }
            if(column || row) return true;
        }
        return false;
    }

    /**
     * Разбиваем поле на участки размером [lenght_win][lenght_win], где lenght_win - количество значений подряд для победы Заполняется в initField(), значением размер_поля - 1
     * @param c - значение для поиска
     * @return
     */
    public static boolean checkWinField(char c){

        int finish = (field.length-(lenght_win-1));
        for (int x=0; x < finish; x++){
            for(int y = 0; y < finish; y++) {
               if(checkWin(c, x, y)) return true;
            }
        }
        return false;
    }


    /**
     * Попытка написать метод поиска координат след хода.
     * Как я себе представляю алгоритм:
     *  - если для победы необходимо собрать 4 знака, то начинаем искать последовательность сначало  из 2, затем  из 3.
     *  - если нахоим, то берем координат либо [i][j++], либо [i++][j], либо [i++][i++]
     *  Непонимаю как обработать ситуацию, когда последовательность идет от конца массива(можно конечно в обратном направлении пройти массив)
     *
     *
     * Метод поиска координат для следующего хода AI.
     *
     * @param c
     */
    public static void search(char c){
        boolean column;
        boolean row;

        int x = 0;
        int y = 0;
        int countColumn = 0;
        int countRow = 0;
        int finalCount = 0;
        int finalX=0;
        int finalY = 0;


            for (int i = 0; i < field.length; i++) {
                column = true;
                row = true;
                for (int j = 0; j < field.length; j++) {
                    column = (column && field[i][j] == c);
                    row = (row && field[j][i] == c);
                    if(column){
                        countColumn ++;
                        x = i;
                        y = j+1;
                    }
                    if(row){
                        countRow ++;
                        x = i+1;
                        y = j;
                    }
                }
                countColumn = 0;
            }
        System.out.println("Надо ставить в I = " + x + " Y = " + y);

    }


    public static void main(String[] args) {
        while (true) {
        playOneRound();
            System.out.println("Play again?");
            if (SCANNER.next().equals("no"))
                break;
        }
    }

    private static void playOneRound() {
        initField();
        printField();
        while (true) {
            humanTurn();
            printField();
            search(DOT_HUMAN);

           //Проверка подебы на поле различного размера
            if(checkWinField(DOT_HUMAN)){
                System.out.println("Вы победили в игре на 5!");
                break;
            }
            //Проверка подебы на поле различного размера

            if (checkWin(DOT_HUMAN)) {
                System.out.println("Вы победили!");
                break;
            }
            if (isFieldFull()) {
                System.out.println("Поле занято!");
                break;
            }
            aiTurn();
            printField();

            //Проверка подебы на поле различного размера
            if(checkWinField(DOT_AI)){
                System.out.println("Компьютер победили в игре!");
                break;
            }
            //Проверка подебы на поле различного размера

            if (checkWin(DOT_AI)) {
                System.out.println("Компьютер победил!");
                break;
            }
            if (isFieldFull()) {
                System.out.println("Все поля заняты!");
                break;
            }
        }
    }
}
