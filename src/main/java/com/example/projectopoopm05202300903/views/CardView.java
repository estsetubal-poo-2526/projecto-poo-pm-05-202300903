package com.example.projectopoopm05202300903.views;

import com.example.projectopoopm05202300903.models.card.Card;
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

    private final Card    card;
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
        // stats[0] = {typeLabel, borderColor, artBgColor}  (display metadata)
        // stats[1+] = {text, fg, bg, fontSize}             (stat labels)
        String[][] stats = card.getCardAppearance(onBoard);

        setStyle(baseStyle(stats[0][1]));

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

        StackPane art = artArea(stats[0][0], stats[0][2]);

        Label desc = new Label(card.getDescription());
        desc.setStyle("-fx-font-size: 8px; -fx-text-fill: #adb5bd;");
        desc.setWrapText(true);
        desc.setMaxWidth(W - 12);
        desc.setAlignment(Pos.CENTER);

        HBox statRow = statsRow(stats);

        root.getChildren().addAll(topRow, art, desc, statRow);
        getChildren().add(root);

        Tooltip.install(this, new Tooltip(
                card.getName() + "\nMana: " + card.getManaCost() + "\n" + card.getDescription()));
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

    private StackPane artArea(String typeLabel, String artBgColor) {
        Rectangle bg = new Rectangle(W - 12, 56);
        bg.setArcWidth(6);
        bg.setArcHeight(6);
        bg.setFill(Color.web(artBgColor, 0.35));

        Label label = new Label(typeLabel);
        label.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: white;");

        StackPane art = new StackPane(bg, label);
        art.setPrefHeight(58);
        art.setMinHeight(58);
        return art;
    }

    /** Renders stat rows from index 1 onwards (index 0 is display metadata). */
    private HBox statsRow(String[][] stats) {
        HBox row = new HBox(6);
        row.setAlignment(Pos.CENTER);
        row.setPadding(new Insets(2, 0, 0, 0));
        for (int i = 1; i < stats.length; i++) {
            String[] s = stats[i];
            row.getChildren().add(stat(s[0], s[1], s[2], s[3]));
        }
        return row;
    }

    private Label stat(String text, String fg, String bg, String fontSize) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: " + fontSize + "; -fx-font-weight: bold; -fx-text-fill: " + fg
                + "; -fx-background-color: " + bg + "; -fx-background-radius: 3; -fx-padding: 1 3;");
        return l;
    }

    public void setSelected(boolean sel) {
        this.selected = sel;
        if (sel) {
            setStyle(style("#1a1d23", "#ffd700", true));
            setTranslateY(-12);
        } else {
            setStyle(baseStyle(card.getCardAppearance(onBoard)[0][1]));
            setTranslateY(0);
        }
    }

    private void applyHover(boolean on) {
        setTranslateY(on ? -6 : 0);
        setStyle(on
                ? style("#1a1d23", "#00d2ff", false)
                : baseStyle(card.getCardAppearance(onBoard)[0][1]));
    }

    private String baseStyle(String borderColor) {
        return style("#1a1d23", borderColor, false);
    }

    private String style(String bg, String border, boolean glow) {
        String base = "-fx-background-color: " + bg + "; -fx-background-radius: 8; "
                + "-fx-border-color: " + border + "; -fx-border-radius: 8; -fx-border-width: 2;";
        if (glow) base += " -fx-effect: dropshadow(gaussian, " + border + ", 14, 0.7, 0, 0);";
        return base;
    }

    public Card    getCard()     { return card; }
    public boolean isSelected()  { return selected; }
}