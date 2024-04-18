# JLang - Kalamandzik użytkownika

JLang to deklaratywny język programowania dla małych i dużych (aplikacji bądź użytkowników).

## Wprowadzenie

Poniżej przedstawiono prosty program w JLangu.

```jlang
no to mamy powitanie rowne 'Witaj świecie!'
nazachodziejest(powitanie)
```

Program ten wypisuje na ekranie napis `Witaj świecie!`, a następnie kończy swoje działanie.
Można tutaj wyróżnić niektóre elementy języka:

- `no to mamy <nazwa_zmiennej>` - deklaracja zmiennej, w tym przypadku zmiennej `powitanie`
- inferowanie typu zmiennej - w tym przypadku typ zmiennej `powitanie` to `sztring`, warto zwrócić uwagę,
  że nie jest on zadeklarowany wprost
- `nazachodziejest(<nazwa_zmiennej>)` - wywołanie funkcji `nazachodziejest` z argumentem `powitanie`
- brak konieczności kończenia linii średnikiem - w JLang należy korzystać z indentacji

## Zmienne

JLang posiada następujące typy zmiennych:

- `sztring` - napis (deklarowany przez użycie cudzysłowów)
- `intem` - liczba całkowita (i32)
- `rzeczywiste` - liczba podwójnej precyzji (double)

Deklaracja zmiennej może ale nie musi zawierać przypisania:

```jlang
no to mamy zmienna rowne 4
```

co jest równoznaczne z:

```jlang
no to mamy zmienna co jest intem
zmienna bedzie drodzy panstwo 4
```

Przykład drugi zawiera instrukcję przypisania.

## Arytmetyka

W JLang arytmetyka działa jak w większości języków programowania i może być używana przy przypisaniach:

```jlang
no to mamy a rowne 2 + 2
no to mamy b rowne a * 2
a bedzie a + 1
```

Odczytywanie wartości ze zmiennej odbywa się zatem poprzez użycie jej nazwy.
Nie jest możliwe wykonywanie działań na zmiennych o różnych typach.

## Funkcje i biblioteka normalna

### Deklarowanie i wywoływanie funkcji

Funkcje w JLang deklaruje się w następujący sposób:

```jlang
ciach ciach funkcja (rzeczywiste z, rzeczywiste y) co jest rzeczywiste tu jest start
    no to mamy wynik rowne z + y
    pach pach wynik
no i tyle
```

Funkcja `ciach ciach` deklaruje funkcję o nazwie `funkcja`, która przyjmuje dwa argumenty: `z` i `y`
rzeczywiste. Funkcja zwraca wynik poprzez użycie słów kluczowych `pach pach`.

Wywołanie funkcji odbywa się poprzez użycie jej nazwy:

```jlang
funkcja(2.0, 3.1)
```

W przypadku gdy funkcja jest bez argumentów wywołuje się ją z pustymi nawiasami:

```jlang
funkcjaBezArgumentów()
```

```jlang
no to mamy wynik rowne funkcja(2.0, 3)
```

### Biblioteka normalna

Dostarczone przez JLang funkcje są dostępne w tzw. bibliotece normalnej.
Wspierane funkcje to `nazachodziejest` oraz `lewarekazapraweucho`.

```jlang
no to mamy powitanie rowne 'Hej, to ja twoja funkcja!'
nazachodziejest(powitanie)
```

Powyżej zaprezentowano przykład funkcji wypisującej napis na ekranie.
Jest to funkcja jednoargumentowa - przyjmująca sztring.

```jlang
no to mamy x co jest intem
lewarkazapraweucho(&x)
```

Powyżej zaprezentowano przykład funkcji modyfikującej zmienną,
która przyjmuje od użytkownika wartość całkowitą, która zostanie przypisana
do zmiennej x. Warto zwrócić uwagę na użycie operatora `&` przed zmienną,
który pozwala na zapisanie wartości w zmiennej - jest to konstrukt podobny
do użycia wskaźników w języku C.

## Kontrola przepływu

W JLang dostępne są instrukcje warunkowe oraz pętle.

### Instrukcje warunkowe

Instrukcje warunkowe w JLang są zaimplementowane w postaci:

```jlang
gdyby tak prawda tu jest start
  nazachodziemamy('Prawdziwe informacje')
no i tyle
```

Należy zwrócić uwagę, że nie są konieczne nawiasy przy ewaluacji warunku.
Warunki mogą być bardziej skomplikowane, załóżmy że mamy zmienną `x` oraz `y`:

```jlang
gdyby tak x oraz y tu jest start
  nazachodziemamy('x i y są prawdziwe')
no i tyle
```

Możliwe są również warunki z użyciem `i` (&&), `albo` (||) a także `==`.

### Pętla

Pętla w JLang jest zaimplementowana w postaci:

```jlang
tak w kolo tu jest start
nazachodziemamy('Nieskończone pętle')
no i tyle
```

Pętle są (z założenia) nieskończone, co jest zgodne z zasadami idiomatycznego JLang.

## Wymagania techniczne

JLang kompiluje się do kodu maszynowego za pomocą backendu LLVM.
Możliwe jest także wygenerowanie kodu w IR, w celu podejrzenia
wyniku. Wymagane jest posiadanie zainstalowanego LLVM i dostpnego w ścieżce (PATH).

Język wspierany jest głównie na systemach UNIX-owych, platforma Windows nie jest aktywnie testowana.

## Inne ograniczenia

Język nie wspiera projektów o ilości plików większej niż 1,
co jest jednak wystarczające do napisania jakiegokolwiek programu. Idiomatyczny
kod w JLang powinien być napisany w jednym pliku.
