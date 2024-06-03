# 1. Skład grupy
- Piotr Karamon(piotrkaramon@student.agh.edu.pl)
- Tomasz Żmuda(tzmuda@student.agh.edu.pl)
# 2. Tytuł(temat) projektu

TutorBridge

Aplikacja bazodanowa zaprojektowana do wsparcia zarówno korepetytorów, jak i
uczniów poszukujących indywidualnych lekcji. Korepetytorzy będą mogli
wyspecyfikować swoje dostępne godziny, swoje specjalizacje(np. matematyka, angielski).
Uczniowie będą mogli rezerwować poszczególne lekcje i wystawiać recenzje
korepetytorom. Przewidujemy opcję wygodnego wyszukiwania korepetytorów
oraz dostępnych godzin(według szukanego przedmiotu, czasu, recenzji etc).


# 3. SZBD i technologie realizacji projektu

Oracle PL/SQL i Java Hibernate

# 4. Link do repozytorium
https://github.com/pkaramon/bazy-danych-2-projekt-Piotr-Karamon-Tomasz-Zmuda

# Funkcje

1. Rejestracja(Tutor & Student)
   + wszyscy podają dane jak imię, nazwisko, email, hasło, numer telefonu
   + korepetytorzy podają swoje specjalizacje np. (angielski
     podstawowy,angielski C1, matematyka licealna etc)
   + uczniowie podają informacje o tym czy są w (podstawówce, liceum etc.)

2. Logowanie(Tutor & Student)

3. Wyznaczenie dostępnych godzin(Tutor)
   Korepetytorzy zaznaczają w których godzinach w tygodniu mają czas by prowadzić zajęcia.

   Usunięcie/modyfikacja dostępnych godzin nie ma wpływu na rezerwacje(nie są
   automatycznie anulowane etc).

4. Modyfikacja(dodanie, usunięcie) specjalizacji(Tutor)

5. Dodanie czasu nieobecności(np. urlop, choroba, etc) (Tutor)
   Korepetytorzy mogą wyznaczyć pewien przedział czasowy w którym to pomimo, że
   zazwyczaj mogą prowadzić zajęcia, to jednak z jakichś powodów nie będą wtedy
   dostępni.

   Jeżeli występują rezerwacje w podanym przedziale czasowym to są automatycznie
   anulowane.

6. Dodanie rezerwacji(Student)
   Student rezerwuje zajęcia u danego tutora.
   Konieczne jest tu sprawdzenie dostępności.

   Możliwa jest rezerwacja zajęć co tydzień lub pojedynczych.

7. Potwierdzenie/Anulowanie rezerwacji(Tutor)

8. Wyświetlenie planu(Tutor) (np. na tydzień/dwa/miesiąc)
   Tutor dostaje informację o tym z kim ma zajęcia i kiedy,
   udostępniany jest mu email oraz numer telefonu w celu
   kontaktu ze studentem.

9. Dodanie recenzji(Student)

10. Wyszukiwanie odpowiedniego tutora(Student)
    Możliwość filtrowania po opiniach(ilość, średnia gwiazdek), specjalizacji,
    czasie(np. student chce znaleźć tutora który jest dostępny w poniedziałki od
    17:00 do 20:00).

