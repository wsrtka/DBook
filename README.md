# DBook
Wprowadzenie:
Projekt ma za zadanie stworzenie prostej aplikacji, która byłaby wstanie obsługiwać działanie szkolnej giełdy podręczników. 

Założenia systemu:
Giełda ma się odbywać w sposób "turowy". Najpierw przynoszone i gromadzone są książki (składane są oferty), a następnie użytkownicy mają możliwość składania zamówień (obydwa procesy na siebie nie zachodzą czasowo).
Klient płaci całościowo za zamówienie, albo w ogóle.
Klientami mogą być uczniowie.
Wszystkie zamówienia/oferty są zatwierdzane przez pracowników systemu po przyniesieniu pieniędzy/książek
Po fazie składania i odbierania zamówień użytkownicy, którzy wcześniej wystawili swoje książki na sprzedaż (i zostały one sprzedane) mogą się zgłosić op odbiór pieniędzy.

Aktorzy:
- Pracownik,
- Klient

Funkcje użytkowników systemu:
Pracownik:
- administrowanie pracy systemu (możliwość usuwania zamówień/ofert),
- wgląd w zamówienia/oferty wszystkich klientów,
- możliwość usuwanie książek z systemu,
- rejestruje się w systemie (podaje: imię, nazwisko, email i swój telefon kotaktowy)
- może wygenerować listę niesprzedanych książek każdego klienta

Klient:
- możliwość składania zamówień i ofert,
- wgląd w dostępne książki (jeszcze nie zamówione),
- rejestruje się w systemie (podaje: imię, nazwisko, email i swoją klasę),
- może odebrać pieniądze ze sprzedanych książek/odebrać niesprzedane książki

![ER](https://user-images.githubusercontent.com/58508596/80842616-5ad5b680-8c02-11ea-85b6-f5fde72e96c3.png)
