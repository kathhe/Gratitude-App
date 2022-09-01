package com.example.gratitude;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "eintrag_table")
public class Eintrag {

    // Autogenerierte ID des Eintrags
    @PrimaryKey(autoGenerate = true)
    private int id;

    // Datum des Eintrags
    @ColumnInfo(name = "datum")
    protected String mDatum;

    // Stimmung des Eintrags
    @ColumnInfo(name = "stimmung")
    protected String mStimmung;

    // Titel des Eintrags
    @ColumnInfo(name = "titel")
    protected String mTitel;

    // Beschreibung des Eintrags
    @ColumnInfo(name = "text")
    protected String mText;

    // Bild des Eintrags (als URI)
    @ColumnInfo(name = "bild")
    protected String mBild;

    // Audio-Aufnahme des Eintrags (als URI)
    @ColumnInfo(name = "audio")
    protected String mAudio;

    /**
     * Konstruktor für die Klasse Eintrag
     * @param mDatum
     * @param mStimmung
     * @param mTitel
     * @param mText
     * @param mBild
     * @param mAudio
     */
    public Eintrag(String mDatum, String mStimmung, String mTitel, String mText, String mBild, String mAudio) {
        this.mDatum = mDatum;
        this.mStimmung = mStimmung;
        this.mTitel = mTitel;
        this.mText = mText;
        this.mBild = mBild;
        this.mAudio = mAudio;
    }

    /**
     * Liefert die ID des Eintrags
     * @return String id;
     */
    public int getId() {
        return id;
    }

    /**
     * Setzt die ID des Eintrags
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Liefert das Datum des Eintrags
     * @return mDatum
     */
    public String getmDatum() {
        return mDatum;
    }

    /**
     * Setzt das Datum des Eintrags
     * @param mDatum
     */
    public void setmDatum(String mDatum) {
        this.mDatum = mDatum;
    }

    /**
     * Liefert die Stimmung des Eintrags
     * @return mStimmung
     */
    public String getmStimmung() {
        return mStimmung;
    }

    /**
     * Setzt die Stimmung des Eintrags
     * @param mStimmung
     */
    public void setmStimmung(String mStimmung) {
        this.mStimmung = mStimmung;
    }

    /**
     * Liefert den Titel des Eintrags
     * @return mTitel
     */
    public String getmTitel() {
        return mTitel;
    }

    /**
     * Setzt den Titel des Eintrags
     * @param mTitel
     */
    public void setmTitel(String mTitel) {
        this.mTitel = mTitel;
    }

    /**
     * Liefert die Beschreibung des Eintrags
     * @return mText
     */
    public String getmText() {
        return mText;
    }

    /**
     * Setzt die Beschreibung des Eintrags
     * @param mText
     */
    public void setmText(String mText) {
        this.mText = mText;
    }

    /**
     * Liefert die Bild-URI des Eintrags
     * @return mBild
     */
    public String getmBild() {
        return mBild;
    }

    /**
     * Setzt die Bild-URI des Eintrags
     * @param mBild
     */
    public void setmBild(String mBild) {
        this.mBild = mBild;
    }

    /**
     * Liefert die Audio-URI des Eintrags
     * @return mAudio
     */
    public String getmAudio() {
        return mAudio;
    }

    /**
     * Setzt die Audio-URI des Eintrags
     * @param mAudio
     */
    public void setmAudio(String mAudio) {
        this.mAudio = mAudio;
    }

    /**
     * Liefert alle Werte des Eintrags
     * @return
     */
    public String getAllValues() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("Eintrag id: %s\r\n", this.id));
        s.append(String.format("Eintrag datum: %s\r\n", this.mDatum));
        s.append(String.format("Eintrag stimmung: %s\r\n", this.mStimmung));
        s.append(String.format("Eintrag titel: %s\r\n", this.mTitel));
        s.append(String.format("Eintrag text: %s\r\n", this.mText));
        s.append(String.format("Eintrag bild: %s\r\n", this.mBild));
        s.append(String.format("Eintrag audio: %s\r\n", this.mAudio));

        return s.toString();
    }
}
