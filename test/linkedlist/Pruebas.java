package linkedlist;
import java.util.*;
public class Pruebas {
    static int fallos = 0;
    static void check(String n, boolean ok){ System.out.println((ok?"OK   ":"FALLA ")+n); if(!ok) fallos++; }
    static <T> List<T> l(Listaenlazada<T> x){ List<T> r=new ArrayList<>(); for(T t:x) r.add(t); return r; }
    static boolean lanza(Runnable r){ try{ r.run(); return false;}catch(ListException e){ return true;} }
    public static void main(String[] a){
        Listaenlazada<Integer> x = new Listaenlazada<>();
        check("vacia", x.empty() && x.size()==0);
        x.insert(5,0); x.insert(7,1); x.insert(6,1); x.insert(4,0);
        check("insert inicio/medio/final", l(x).equals(Arrays.asList(4,5,6,7)));
        check("insert fuera de rango", lanza(()->x.insert(1,9)) && lanza(()->x.insert(1,-1)));
        check("get", x.get(0)==4 && x.get(3)==7);
        check("get invalido", lanza(()->x.get(4)) && lanza(()->x.get(-1)));
        check("remove medio", x.remove(1)==5 && l(x).equals(Arrays.asList(4,6,7)));
        check("remove inicio", x.remove(0)==4 && l(x).equals(Arrays.asList(6,7)));
        check("remove final", x.remove(1)==7 && l(x).equals(Arrays.asList(6)));
        check("remove invalido", lanza(()->x.remove(1)));
        x.clear();
        check("clear", x.size()==0 && x.empty() && l(x).isEmpty());
        check("remove en vacia", lanza(()->x.remove(0)));
        // invertir
        x.invertir(); check("invertir vacia", x.size()==0);
        x.append(1); x.invertir(); check("invertir 1", l(x).equals(Arrays.asList(1)));
        x.append(2); x.append(3); x.invertir();
        check("invertir 3", l(x).equals(Arrays.asList(3,2,1)) && x.size()==3);
        x.append(0); check("append tras invertir", l(x).equals(Arrays.asList(3,2,1,0)));
        // concatenar
        Listaenlazada<Integer> v = new Listaenlazada<>();
        Listaenlazada<Integer> y = new Listaenlazada<>(); y.append(8); y.append(9);
        v.concatenar(y); check("vacia + y", l(v).equals(Arrays.asList(8,9)) && v.size()==2);
        v.concatenar(new Listaenlazada<>()); check("+ vacia", v.size()==2);
        v.concatenar(y); check("v + y", l(v).equals(Arrays.asList(8,9,8,9)) && v.size()==4);
        check("y intacta", l(y).equals(Arrays.asList(8,9)) && y.size()==2);
        y.set(100,0); check("independencia de nodos", v.get(0)==8);
        v.concatenar(v); check("consigo misma", l(v).equals(Arrays.asList(8,9,8,9,8,9,8,9)) && v.size()==8);
        check("concatenar null", lanza(()->v.concatenar(null)));
        v.append(1); check("append tras concatenar", v.get(8)==1 && v.size()==9);
        // circular / doblemente enlazada / cursor
        Listaenlazada<Integer> c = new Listaenlazada<>();
        check("cursor en vacia lanza", lanza(()->c.next()) && lanza(()->c.previous()) && lanza(()->c.actual()));
        c.irInicio(); c.irFinal(); check("irInicio/irFinal en vacia no fallan", c.empty());
        c.append(1); c.append(2); c.append(3);
        check("cursor inicial en cabeza", c.actual()==1);
        c.next(); c.next(); check("next hasta cola", c.actual()==3);
        c.next(); check("next desde cola vuelve a cabeza", c.actual()==1);
        c.previous(); check("previous desde cabeza va a cola", c.actual()==3);
        c.previous(); check("previous normal", c.actual()==2);
        c.irInicio(); check("irInicio", c.actual()==1);
        c.irFinal(); check("irFinal", c.actual()==3);
        c.irFinal(); List<Integer> rev=new ArrayList<>(); for(int i=0;i<3;i++){ rev.add(c.actual()); c.previous(); }
        check("recorrido hacia atras", rev.equals(Arrays.asList(3,2,1)));
        c.insert(0,0); c.irInicio(); check("insert en 0 cabeza nueva", c.actual()==0 && l(c).equals(Arrays.asList(0,1,2,3)));
        c.previous(); check("ant de nueva cabeza es la cola", c.actual()==3);
        c.insert(9,2); check("insert medio", l(c).equals(Arrays.asList(0,1,9,2,3)) && c.get(2)==9 && c.get(4)==3);
        c.irInicio(); c.remove(0); check("remove cabeza mueve cursor", c.actual()==1 && l(c).equals(Arrays.asList(1,9,2,3)));
        c.irFinal(); c.remove(3); check("remove cola mueve cursor a cabeza", c.actual()==1 && l(c).equals(Arrays.asList(1,9,2)));
        c.previous(); check("tras remove cola, previous es nueva cola", c.actual()==2);
        c.invertir(); check("invertir circular", l(c).equals(Arrays.asList(2,9,1)) && c.actual()==2);
        c.irInicio(); c.previous(); check("tras invertir previous desde cabeza es cola", c.actual()==1);
        c.irInicio(); c.next(); check("tras invertir next", c.actual()==9);
        check("indexOf circular", c.indexOf(1)==2 && c.indexOf(77)==-1);
        c.removeObj(9); c.removeObj(2); check("quedar con uno", c.size()==1 && l(c).equals(Arrays.asList(1)));
        c.next(); check("uno: next", c.actual()==1); c.previous(); check("uno: previous", c.actual()==1);
        c.remove(0); check("vaciar", c.empty() && lanza(()->c.actual()));
        c.append(5); check("append tras vaciar", c.actual()==5 && c.size()==1);
        Listaenlazada<Integer> g = new Listaenlazada<>(); for(int i=0;i<10;i++) g.append(i);
        boolean okGet=true; for(int i=0;i<10;i++) okGet &= g.get(i)==i; check("get desde ambos extremos", okGet);
        System.out.println(fallos==0 ? "TODO OK" : "FALLOS: "+fallos);
        System.exit(fallos);
    }
}
