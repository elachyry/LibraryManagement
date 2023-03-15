
package Models;

public class Favorite {
    private int Id;
    private int Member_Id;
    private int Book_Id;

    
    public Favorite() {
    }

    public Favorite(int Id, int Member_Id, int Book_Id) {
        this.Id = Id;
        this.Member_Id = Member_Id;
        this.Book_Id = Book_Id;
    }

    public int getBook_Id() {
        return Book_Id;
    }

    public void setBook_Id(int Book_Id) {
        this.Book_Id = Book_Id;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getMember_Id() {
        return Member_Id;
    }

    public void setMember_Id(int Member_Id) {
        this.Member_Id = Member_Id;
    }
}
