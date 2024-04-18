# JLangCompiler

## Introduction

The following is a course project for course "Formal Languages and Compilers".

## Requirements

- JDK 21 (compatible with gradle 8.5)

## Code generation

In order to generate code you have to run configuration named `generateGrammarSource` which you can do either by running it directly in Intelij IDE or by command prompt like:

```terminal
gradle generateGrammarSource
```

This will generate antlr sources, which will be placed in the `build` directory.
Now you should be able to use them by normally importing them in the code.

## Sample

As of now the extent of language's possibilities are shown in the sample bellow:

```
no to mamy x co jest intem
no to mamy y rowne 1.2
c bedzie drodzy panstwo 2 * x + (2. - 3)
lewa reka za prawe ucho ('no i co')
na zachodzie jest ('tak ze string moze miec whitespacey w sobie')

// function declaration
ciach ciach funkcja ( no to mamy x co jest intem ) tu jest start
nazachodziemamy('message')
no i tyle

// scoped loop
tak w kolo tu jest start
nazachodziemamy('message')
no i tyle

// scoped if statement
gdyby tak prawda i klamstwo tu jest start
nazachodziemamy('message')
no i tyle

```

From beginning to end these are:

- variable decleration `no to mamy <id> co jest <typ>`
- variable assignment `no to mamy <id> rowne <id or computable statement>`
- variable assignment but to already declared variable
- read operation with uncomputable string
- write operation with uncomputable string
