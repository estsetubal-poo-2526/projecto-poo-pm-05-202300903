package com.example.projectopoopm05202300903.views;

import com.example.projectopoopm05202300903.models.card.Card;
import com.example.projectopoopm05202300903.models.card.SpellCard;
import com.example.projectopoopm05202300903.models.card.UnitCard;
import com.example.projectopoopm05202300903.models.enums.SpellType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class CardView extends StackPane {

    private static final double W = 108;
    private static final double H = 155;

    private final Card card;
    private final boolean onBoard;
    private boolean selected = false;

    public CardView(Card card, boolean faceDown, boolean onBoard) {
        this.card    = card;
        this.onBoard = onBoard;

        setPrefSize(W, H);
        setMaxSize(W, H);
        setMinSize(W, H);
        setCursor(Cursor.HAND);

        if (faceDown) buildFaceDown();
        else          buildFaceUp();

        setOnMouseEntered(e -> { if (!selected) applyHover(true); });
        setOnMouseExited(e  -> { if (!selected) applyHover(false); });
    }

    private void buildFaceDown() {
        setStyle(style("#2c3e50", "#7f8c8d", false));
        Label q = new Label("?");
        q.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #7f8c8d;");
        getChildren().add(q);
    }

    private void buildFaceUp() {
        setStyle(baseStyle());

        boolean isUnit = card instanceof UnitCard;

        VBox root = new VBox(4);
        root.setPadding(new Insets(6));
        root.setPrefSize(W, H);
        root.setAlignment(Pos.TOP_CENTER);

        HBox topRow = new HBox(5);
        topRow.setAlignment(Pos.CENTER_LEFT);

        StackPane manaBadge = manaBadge(card.getManaCost());

        Label nameLabel = new Label(card.getName());
        nameLabel.setStyle("-fx-font-size: 9px; -fx-font-weight: bold; -fx-text-fill: white;");
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(W - 38);
        HBox.setHgrow(nameLabel, Priority.ALWAYS);

        topRow.getChildren().addAll(manaBadge, nameLabel);

        StackPane art = artArea(isUnit);

        Label desc = new Label(card.getDescription());
        desc.setStyle("-fx-font-size: 8px; -fx-text-fill: #adb5bd;");
        desc.setWrapText(true);
        desc.setMaxWidth(W - 12);
        desc.setAlignment(Pos.CENTER);

        HBox stats = statsRow(isUnit);

        root.getChildren().addAll(topRow, art, desc, stats);
        getChildren().add(root);

        Tooltip.install(this, new Tooltip(card.getName() + "\nMana: " + card.getManaCost() + "\n" + card.getDescription()));
    }

    private StackPane manaBadge(int mana) {
        Circle circle = new Circle(12);
        circle.setFill(Color.web("#4dabf7"));
        circle.setStroke(Color.web("#1c7ed6"));
        circle.setStrokeWidth(1.5);
        Label lbl = new Label(String.valueOf(mana));
        lbl.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: white;");
        StackPane badge = new StackPane(circle, lbl);
        badge.setMinSize(24, 24);
        badge.setMaxSize(24, 24);
        return badge;
    }

    private StackPane artArea(boolean isUnit) {
        Rectangle bg = new Rectangle(W - 12, 56);
        bg.setArcWidth(6);
        bg.setArcHeight(6);
        bg.setFill(isUnit ? Color.web("#c0392b", 0.35) : Color.web("#8e44ad", 0.35));

        Label typeLabel = new Label(isUnit ? "UNIDADE" : "MAGIA");
        typeLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: white;");

        StackPane art = new StackPane(bg, typeLabel);
        art.setPrefHeight(58);
        art.setMinHeight(58);
        return art;
    }

    private HBox statsRow(boolean isUnit) {
        HBox row = new HBox(6);
        row.setAlignment(Pos.CENTER);
        row.setPadding(new Insets(2, 0, 0, 0));

        if (isUnit) {
            UnitCard unit = (UnitCard) card;

            Label atk = stat("ATK " + unit.getAttack(), "#ffd700", "#e74c3c44");
            String hpText = onBoard
                    ? unit.getCurrentHealth() + "/" + unit.getMaxHealth()
                    : String.valueOf(unit.getMaxHealth());
            Label hp = stat("HP " + hpText, "#ff6b6b", "#c0392b44");
            row.getChildren().addAll(atk, hp);

            if (onBoard && unit.hasAttackedThisTurn()) {
                Label used = new Label("(usado)");
                used.setStyle("-fx-font-size: 7px; -fx-text-fill: #868e96;");
                row.getChildren().add(used);
            }
        } else {
            SpellCard spell = (SpellCard) card;
            boolean isDmg = spell.getType() == SpellType.DAMAGE;
            String prefix = isDmg ? "DMG " : "CURA ";
            String color  = isDmg ? "#ff6b6b" : "#69db7c";
            row.getChildren().add(stat(prefix + spell.getEffectValue(), color, "#00000055"));
        }
        return row;
    }

    private Label stat(String text, String fg, String bg) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 9px; -fx-font-weight: bold; -fx-text-fill: " + fg +
                   "; -fx-background-color: " + bg + "; -fx-background-radius: 3; -fx-padding: 1 3;");
        return l;
    }

    public void setSelected(boolean sel) {
        this.selected = sel;
        if (sel) {
            setStyle(style("#1a1d23", "#ffd700", true));
            setTranslateY(-12);
        } else {
            setStyle(baseStyle());
            setTranslateY(0);
        }
    }

    private void applyHover(boolean on) {
        setTranslateY(on ? -6 : 0);
        setStyle(on ? style("#1a1d23", "#00d2ff", false) : baseStyle());
    }

    private String baseStyle() {
        boolean isUnit = card instanceof UnitCard;
        String border  = isUnit ? "#e74c3c" : "#8e44ad";
        return style("#1a1d23", border, false);
    }

    private String style(String bg, String border, boolean glow) {
        String base = "-fx-background-color: " + bg + "; -fx-background-radius: 8; " +
                      "-fx-border-color: " + border + "; -fx-border-radius: 8; -fx-border-width: 2;";
        if (glow) base += " -fx-effect: dropshadow(gaussian, " + border + ", 14, 0.7, 0, 0);";
        return base;
    }

    public Card getCard()   { return card; }
    public boolean isSelected() { return selected; }
}