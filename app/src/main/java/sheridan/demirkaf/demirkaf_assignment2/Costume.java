package sheridan.demirkaf.demirkaf_assignment2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Costume implements Parcelable {
    private String name;
    private String difficulty;
    private ArrayList<String> materials;
    private String instructions;
    private String base64Image;

    public static final Parcelable.Creator<Costume> CREATOR = new Parcelable.Creator<Costume>() {
        @Override
        public Costume createFromParcel(Parcel in) {
            return new Costume(in);
        }

        @Override
        public Costume[] newArray(int size) {
            return new Costume[size];
        }
    };

    Costume(String name, String difficulty, ArrayList<String> materials, String instructions) {
        this.name = name;
        this.difficulty = difficulty;
        this.materials = materials;
        this.instructions = instructions;
    }

    private Costume(Parcel parcel) {
        name = parcel.readString();
        difficulty = parcel.readString();
        materials = parcel.readArrayList(ClassLoader.getSystemClassLoader());
        instructions = parcel.readString();
        base64Image = parcel.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getDifficulty() {
        return difficulty;
    }

    void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    ArrayList<String> getMaterials() {
        return materials;
    }

    void setMaterials(ArrayList<String> materials) {
        this.materials = materials;
    }

    String getInstructions() {
        return instructions;
    }

    void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    String getBase64Image() {
        return base64Image;
    }

    void setBase64Image(String bitmapUri) {
        this.base64Image = bitmapUri;
    }
    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(difficulty);
        dest.writeList(materials);
        dest.writeString(instructions);
        dest.writeString(base64Image);
    }

    @Override
    public String toString() {
        return "Costume{" +
                "name='" + name + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", materials=" + materials +
                ", instructions='" + instructions + '\'' +
                ", bitmapUri=" + base64Image + '\'' +
                '}';
    }
}
