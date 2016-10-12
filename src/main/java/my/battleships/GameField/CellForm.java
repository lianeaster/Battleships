package my.battleships.GameField;

public enum CellForm {
    EMPTY ('.'),
    MISSED ('O'),
    DECK ('S'),
    MINE ('M'),
    DAMAGED('X');

    Character form;

    CellForm(Character form) {
        this.form = form;
    }

    @Override
    public String toString() {
        return form.toString();
    }
}
