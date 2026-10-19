import java.util.LinkedList;

public class Main {
    static final int N = 10000;
    static volatile long control = 0;

    static long medir(Runnable tarea, int repeticiones) {
        long inicio = System.nanoTime();
        for (int i = 0; i < repeticiones; i++) tarea.run();
        return (System.nanoTime() - inicio) / repeticiones;
    }

    public static void main(String[] args) {
        int[] arreglo = new int[N];
        LinkedList<Integer> simple = new LinkedList<>(); // Java LinkedList es DOBLEMENTE enlazada
        ListaSimple listaSimple = new ListaSimple();
        for (int i = 0; i < N; i++) { arreglo[i] = i; listaSimple.agregarFinal(i); simple.add(i); }
        // Calentamiento de JVM
        for (int i = 0; i < 1000; i++) { control += arreglo[N/2]; control += listaSimple.buscar(N-1) ? 1 : 0; }
        System.out.println("COMPARACION DE ESTRUCTURAS - N=" + N);
        System.out.println("Acceso indice arreglo: " + medir(() -> control += arreglo[N/2], 100000) + " ns/op");
        System.out.println("Busqueda lista simple: " + medir(() -> control += listaSimple.buscar(N-1) ? 1 : 0, 1000) + " ns/op");
        System.out.println("Busqueda lista doble: " + medir(() -> control += simple.contains(N-1) ? 1 : 0, 1000) + " ns/op");
        System.out.println("Busqueda arreglo: " + medir(() -> { for (int x : arreglo) if (x == N-1) { control++; break; } }, 1000) + " ns/op");
        System.out.println("Complejidad teorica: acceso por indice arreglo O(1); busqueda por valor O(N) en las tres.");
        System.out.println("Memoria teorica: arreglo O(N), lista simple O(N), lista doble O(N).");
        System.out.println("Nota: mediciones aproximadas; dependen de equipo, JVM y calentamiento.");
    }
}
class ListaSimple {
    static class Nodo { int valor; Nodo siguiente; Nodo(int v) { valor = v; } }
    private Nodo cabeza, cola;
    void agregarFinal(int valor) {
        Nodo nuevo = new Nodo(valor);
        if (cabeza == null) cabeza = nuevo;
        else cola.siguiente = nuevo;
        cola = nuevo;
    }
    boolean buscar(int buscado) {
        Nodo actual = cabeza;
        while (actual != null) {
            if (actual.valor == buscado) return true;
            actual = actual.siguiente;
        }
        return false;
    }
}
