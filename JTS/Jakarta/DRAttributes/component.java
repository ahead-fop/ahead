package Jakarta.DRAttributes;

public abstract class component {
   public String name;
   public static component current = null;

   public component  Parent;
   public component  Child[];
   public konditions Cpost;
   public konditions Cpostres;

   public component( String name ) {
      this.name = name;
   }

   public abstract boolean topdownDRC( konditions env );
   public abstract boolean bottomupDRC( konditions env );
   public abstract String  explain();
   public abstract String  and();
   public abstract String  another();

   public konditions merge() {
      return Child[0].Cpostres;
   }

   public konditions merge_alternative() {
      int i;
      konditions k;

      k = Child[0].Cpostres.copy();
      for (i=1; i<=Child.length; i++)
         k.merge(Child[i].Cpostres);
      return k;
   }
}
