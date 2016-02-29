
/*
* This is free software; you can redistribute it and/or modify it under
* the terms of version 3 of the GNU General Public License as published
* by the Free Software Foundation.
*
* Copyright 2012 Milan Draganić, Tomislav Novak - NetAkademija
*/
package Projekt.UI;

import Projekt.Data.DataBase;
import Projekt.Logic.*;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Sučelje za komunikaciju sa korisnikom temeljeno na konzoli.
 * Korisniku je u mogućnosti odabrati i igranje jedne od igara.
 *
 * @author Milan Draganić Tomislav Novak
 */
abstract class ConsoleUI {

    public static Scanner sc = new Scanner(System.in);

    /**
     * Metoda koja pokrece glavni meni konzolnog UI. Ispisuje dobrodoslicu
     * korisniku te mu ispisuje meni za odabir jednu od dvije grupe igara: -
     * BoardGames: logicke igrice koje ukljucuju dva igraca i igraju se na
     * ploci. - CardGames: igre sa kartama koje mogu ukljucivati i vise igraca.
     * Kada korisnik odabere grupu igara pokrece se boardGamesUI() metoda ili
     * cardGamesUI() metoda. Meni mora ispisati i opciju za zavrsetak igranja.
     * Program zavrsava kada korisnik odabere opciju za kraj. Ukoliko korisnik
     * odabere nepostojecu opciju program ga o tome mora obavjestiti i ponovno
     * ponuditi unos opcije.
     */
    public static void startUI() {

        System.out.println("Dobrodosli u NAJGames igre. ");

        while (true) {
            System.out.println("Odaberite skupinu igara koju zelite igrati:\n [b] Board Games - logicke igrice koje ukljucuju dva igraca i igraju se na ploci. \n [c] CardGames - igre sa kartama koje mogu ukljucivati i vise igraca.\n [h] Highscores \n [x] Izlaz!");

            switch (sc.next().charAt(0)) {

                case 'b': {
                    boardGamesUI();
                    break;
                }
                case 'c': {
                    cardGamesUI();
                    break;
                }
                case 'h': {
                    System.out.println(showHighScores());
                    continue;
                }
                case 'x': {
                    System.out.println("Hvala na koristenju programa!");
                    break;
                }
                default: {
                    continue;
                }

            }

            break;

        }
    }

    /**
     * Metoda koja stvara listu igrača poredanih od onoga sa najvećim rezultatom
     * (engl.score) prema onom s najmanjim rezultatom.
     *
     * @return String objekt sa popisom igrača.
     */
    private static String showHighScores() {

        StringBuilder sb = new StringBuilder();
        Player[] highscores = DataBase.getInstance().readPlayer();

/*
* Potrebno je sortirati polje. Player implementira Comparable sućelje. *
*/
        Arrays.sort(highscores);

        for (int i = 0; i < highscores.length; i++) {
            Player player = highscores[i];
            sb.append((i + 1)).append(". ").append(player).append("\n");
        }
        return sb.toString();
    }

    /**
     * Metoda koja ispisuje raspolozive Board igre i nudi korisniku odabir jedne
     * od igara: - TicTacToe - ConnectFour - Reversi Meni mora ispisati i opciju
     * za povretak na glavni meni. Kada korisnik odabere igru po zelji pokrece
     * se metoda playBoardGame(int). Ukoliko korisnik odabere nepostojecu opciju
     * program ga o tome mora obavjestiti i ponovno ponuditi unos opcije.
     */
    private static void boardGamesUI() {
        while (true) {
            System.out.println("Odaberite igru koju zelite igrati:\n [1] TicTacToe \n [2] ConnectFour\n [3] Reversi\n [p] Povratak na glavni meni. \n [x] Izlaz!");

            switch (sc.next().charAt(0)) {

                case '1': {
                    playBoardGameUI(1);
                    break;
                }
                case '2': {
                    playBoardGameUI(2);
                    break;
                }
                case '3': {
                    playBoardGameUI(3);
                    break;
                }
                case 'p': {
                    startUI();
                    break;
                }
                case 'x': {
                    System.out.println("Hvala na koristenju programa!");
                    break;
                }
                default: {
                    System.out.println("Krivi unos znaka za odabir! Pokusajte ponovo.");
                    continue;
                }

            }

            break;

        }
    }

    /**
     * Metoda kreira igrace koristeci metodu createBoardPlayer(int, char) i
     * BoardGame objekt koristeci BoardGameFactory klasu i implemetira konzolni
     * IO prema korisniku za igranje BoardGame igrice: 1. ispisuje pozicije
     * polja (getBoardHelp()) 2. dok postoje raspolozivi potezi, tj. nitko nije
     * pobijedio i ploca nije ispunjena do kraja (checkIfThereAreAnyMovesLeft())
     * ispisuje: 2.1. koji igrac je na redu (getActivePlayer()). 2.2. postavlja
     * igracev odabir polja (makeAMove(int)) .3. ispisuje status ploce nakon
     * poteza igraca (getBoardStatus()) 3. ispisuje stanje na kraju igre ukoliko
     * postoji pobjednik (doWeHaveAWinner()) i njegovo ime (getWinner()) Metoda
     * koja kreira i pokrece jednu Board igru. Metoda slijedi proceduru: <br> 1.
     * kreiranje igre i igraca<br> 2. pokretanje igre<br> 3. ispis dobrodoslice
     * i uputa<br> 4. igranje dok god postoje slobodni potezi<br> 5. objava
     * rezultata igre i pozdrav<br>
     *
     * @param type tip igre koju korisnik zeli igrati. Vidi boardGamesUI()
     * metodu i popis raspolozivih igara.
     */
    private static void playBoardGameUI(int type) {

        BoardGameType bt;
/* kreiranje igre i igraca */
        BoardGame game;
        switch (type) {
            case 1: {
                game = BoardGameFactory.createGame(BoardGameType.TICTACTOE);
                bt = BoardGameType.TICTACTOE;
                break;
            }
            case 2: {
                game = BoardGameFactory.createGame(BoardGameType.CONNECTFOUR);
                bt = BoardGameType.CONNECTFOUR;
                break;
            }
            case 3: {
                game = BoardGameFactory.createGame(BoardGameType.REVERSI);
                bt = BoardGameType.REVERSI;
                break;
            }
            default: {
                game = null;
                bt = null;
                System.out.println("Krivi odabir igrice!");
                startUI();
                break;
            }
        }

        System.out.printf("Kreiramo %d igraca. \n", game.getNumberOfPlayers());
        int playerNo = 0;
        do {
            try {
                playerNo = game.addPlayer(createBoardPlayer(game));
            } catch (PlayerInitException ex) {
                System.out.println(ex.getMessage());
            } catch (GameStartedException ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println(playerNo);
        } while (playerNo < game.getNumberOfPlayers());

/* pokretanje igre */
        try {
            game.startGame();
        } catch (GameStartedException ex) {
            System.out.println(ex.getMessage());
        } catch (GameInitException ex) {
            System.out.println(ex.getMessage());
        }

/* ispis dobrodoslice i uputa */
        System.out.println("Dobrodosli u igru:" + bt);
        System.out.println(game.getRules());

        System.out.printf("Spremite se i zapamtite pozicije polja.\n%s\n",
                game.getBoardHelp());

/* igranje dok god postoje slobodni potezi */

        while (game.hasMovesLeft()) {
            Player pl = game.getActivePlayer();
            System.out.printf("Na redu je: %s, make your move: ", pl);
            int move;
            if (pl instanceof BoardDroid) {
                move = ((BoardDroid) pl).getMove();
                System.out.println(move);
            } else {
                move = sc.nextInt();
            }
            if (!game.placeMove(move)) {
                System.out.println("Stavite potez na slobodno mjesto!");
            }
            System.out.println(game);
        }
/* objava rezultata igre i pozdrav */
        Player winner = game.getWinner();
        if (winner == null) {
            System.out.println("A sad boks, jer ste u ovoj igri jednaki.");
        } else {
            winner.setScore(winner.getScore() + 1); //povecati score za igrača.
            if (!(winner instanceof BoardDroid)) {//ako nije droid..
                DataBase.getInstance().updatePlayer(winner);//osvježi ga u bazi.
            }

            System.out.printf("Wooooo!! A pobjednik je %s !! \n", winner);
        }
        startUI();//ponovo pitaj korisnika da li želi igrati igre.

    }

    /**
     * Metoda koja radi objekt igrača Board-type igara. Ovisno o tome kakav tip igrača trebamo
     * stvara se novi objekt igrača.
     *
     * @param game Igra za koju se stvara igrač.
     * @return objekt igrača.
     */
    private static BoardPlayer createBoardPlayer(BoardGame game) {
        while (true) {
            System.out.printf("Kreiranje igraca:\n");
            System.out.print("Tip igraca: [h]Human, [d]Droid: ");
            char type = sc.next().charAt(0);
            System.out.print("Ime: ");
            String name = sc.next();
            System.out.print("Znak: ");
            char mark = sc.next().charAt(0);

            switch (type) {
                case 'h':
                    BoardPlayer playa = new BoardPlayerFactoryHuman().createPlayer(game, name, mark, 0);
                    Player playerDB = DataBase.getInstance().readPlayer(playa);
                    if (playerDB == null) {
                        DataBase.getInstance().createPlayer(playa);
                    } else {
                        playa.setScore(playerDB.getScore());
                    }
                    return playa;

                case 'd':
                    return new BoardPlayerFactoryDroid().createPlayer(game, name, mark, 0);
                default:
                    System.out.println("Ne postoji takav tip igraca");
                    break;

            }
        }

    }

    private static void cardGamesUI() {
    }
}