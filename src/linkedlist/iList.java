/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package linkedlist;

import java.util.Iterator;

/**
 *
 * @author edera
 */
public interface iList<T> {
        public void append(T elemento) throws ListException;
    public void insert(T elemento,int index) throws ListException;
    public T remove(int index) throws ListException;
    public boolean removeObj (T elemento) throws ListException;
    public int indexOf (T elemento);
    public T get(int index) throws ListException;
    public void set (T elemento, int index) throws ListException;
    public void clear();
    public boolean empty();
    public int size();
    public Iterator<T> Iterator();
    public void invertir();
    public void concatenar(Listaenlazada<T> otraLista) throws ListException;
    public void irInicio();
    public void irFinal();
    public void next() throws ListException;
    public void previous() throws ListException;
    public T actual() throws ListException;
}
