# EnergyCostPayoff
 Program symulujący rozliczenia kosztów za prąd

w procesie biorą udział następujący aktorzy: Najemca, Kontroler, Zarządca.

Wymienieni aktorzy uzyskują dostęp do systemu za pośrednictwem osobnych aplikacji: 

# NajemcaApp 
 - oferującej interfejs najemcy - osoby, do której przypisany jest lokal, 
 
# KontrolerApp 
 - oferującej interfejs kontrolera - osoby dokonującej pomiary stanów liczników, 
 
# ZarządcaApp 
 - oferującej interfejs zarządcy - osoby, która zarządza wieloma nieruchomościami.

# Najemca: 
posiada konto w systemie, ma dostęp do informacji o wymaganych opłatach za prąd oraz historii rozliczeń, zobowiązany jest do wnoszenia zryczałtowych opłat za prąd (za pośrednictwem NajemcaApp).

# Kontroler: 
posiada konto w systemie, ma dostęp do informacji o zleceniach wykonania odczytu liczników, wprowadza wyniki odczytów (za pośrednictwem KontrolerApp).

# Zarządca: 
posiada konto w systemie, definiuje koszty zryczałtowanych opłat za prąd dla poszczególnych lokali, zleca zadania wykonania odczytu liczników, uruchamia rozliczenia kosztów za prąd z uwzględnieniem stanu licznika głównego oraz zużycia na części wspólne (za pośrednictwem ZarządcaApp).

