# akty-prawne
#### Przeglądarka aktów prawnych - projekt z przedmiotu Programowanie Obiektowe na AGH

```
Użycie:  akty-prawne.jar ŚCIEŻKA [-l] [ZAPYTANIE]
Wyświetla całość lub fragment Konstytucji RP lub Ustawy o ochronie konkurencji i konsumentów.

  -l    wyświetla spis treści

Zapytanie może składać się z następujących części (kropki i polskie znaki są opcjonalne):
  dział 1 rozdział 2 art. 3 ust. 4 pkt 5 lit. a-b

Przykłady:
  akty-prawne.jar konstytucja.txt -l                     - wyświetla spis treści konstytucji
  akty-prawne.jar konstytucja.txt "rozdział VIII"        - wyświetla treść rozdziału "Sądy i Trybunały"
  akty-prawne.jar konstytucja.txt "art. 194 ust. 1"      - wyświetla treść artykułu 194, ustęp 1
  akty-prawne.jar uokik.txt -l "dział III rozdział 1-2"  - wyświetla spis treści 2 pierwszych rozdziałów 3 działu

© 2017  Piotr Janczyk.  Licencja MIT.
```
