#include <stdio.h>
#include <zconf.h>
#include <stdbool.h>
#include <malloc.h>
#include <string.h>

#define LCHILD(x) 2 * x + 1
#define RCHILD(x) 2 * x + 2
#define PARENT(x) (x - 1) / 2


typedef struct node {
    float priority;
    int data;
} node;

typedef struct BinHeap {
    long size;
    node *A;
} BinHeap;

BinHeap CreateBinHeap(){
    BinHeap bh;
    bh.size = 0;
    return bh;
}

bool isEmpty(BinHeap bh){
    if (bh.size == 0) return true;
    else return false;
}

void empty(BinHeap bh){
    if (isEmpty(bh)) printf("1\n");
    else printf("0\n");
}

void insert(BinHeap *bh, int data, float priority){
    if (priority < 0){
        fprintf(stderr, "error, priority < 0\n");
        return;
    }

    if(bh->size) {
        bh->A = realloc(bh->A, (bh->size + 1) * sizeof(node));
    } else {
        bh->A = malloc(sizeof(node));
    }

    node node;
    node.data = data;
    node.priority = priority;
    long i = (bh->size)++;
    while(i && node.priority < bh->A[PARENT(i)].priority) {
        bh->A[i] = bh->A[PARENT(i)];
        i = PARENT(i);
    }
    bh->A[i] = node;
}

void top(BinHeap bh){
    if (bh.size == 0){
        printf("\n");
    } else{
        printf("%d\n", bh.A[0].data);
    }
}

void swap(node *n1, node *n2) {
    node tmp = *n1;
    *n1 = *n2;
    *n2 = tmp;
}

void heapify(BinHeap *bh, int i){
    int smallest = (LCHILD(i) < bh->size && bh->A[LCHILD(i)].priority < bh->A[i].priority) ? LCHILD(i) : i ;
    if(RCHILD(i) < bh->size && bh->A[RCHILD(i)].priority < bh->A[smallest].priority) {
        smallest = RCHILD(i) ;
    }
    if(smallest != i) {
        swap(&(bh->A[i]), &(bh->A[smallest])) ;
        heapify(bh, smallest) ;
    }
}

void pop(BinHeap *bh){
    //top(*bh);
    bh->A[0] = bh->A[--(bh->size)] ;
    bh->A = realloc(bh->A, bh->size * sizeof(node)) ;
    heapify(bh, 0) ;
}

void print(BinHeap *bh){
    int i ;
    for(i = 0; i < bh->size; i++) {
        printf("(%d %f) ", bh->A[i].data, bh->A[i].priority) ;
    }
    printf("\n");
}

void priority(BinHeap *bh, int data, float newPriority){
    long i = 0, k;
    while (i < bh->size){
        if (bh->A[i].data == data){
            if (newPriority < bh->A[i].priority){
                bh->A[i].priority = newPriority;
                k = i;
                while(k && newPriority < bh->A[PARENT(k)].priority) {
                    bh->A[k] = bh->A[PARENT(k)];
                    k = PARENT(k);
                }
                bh->A[k].priority = newPriority;
                bh->A[k].data = data;
            }
        }
        i++;
    }
}

void prim(){
    int n, m;
    scanf("%d%d", &n, &m);

    float Ar[n][n];

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            if (i == j) Ar[i][j] = 0;
            else Ar[i][j] = -1;
        }
    }

    int u, v;
    float w;
    for (int i = 0; i < m; i++){
        scanf("%d%d%f", &u, &v, &w);
        Ar[u-1][v-1] = w;
        Ar[v-1][u-1] = w;
    }

    bool known[n];
    int path[n];
    float cost[n];
    for (int i = 0; i < n; i++){
        known[i] = false;
        path[i] = -1;
        cost[i] = INT_MAX;
    }

    BinHeap bh = CreateBinHeap();
    for (int i = 0; i < n; i++){
        insert(&bh, i, INT_MAX);
    }

    priority(&bh, 0, 0);
    cost[0] = 0;

    while (bh.size != 0){
        known[bh.A[0].data] = true;
        int a = bh.A[0].data;
        float b = bh.A[0].priority;
        pop(&bh);
        for (int i = 0; i < n; i++) {
            if (Ar[a][i] > 0){
                if (known[i] == false){
                    if (b != INT_MAX) {
                        if (cost[i] > Ar[a][i]) {
                            priority(&bh, i, Ar[a][i]);
                            path[i] = a;
                            cost[i] = Ar[a][i];
                        }
                    }
                }
            }
        }
    }
    float sum = 0;
    for (int i = 0; i < n; i++){
        if (cost[i] != INT_MAX && path[i] != -1){
            if (i < path[i]) printf("%d %d %f\n", i+1, path[i]+1, cost[i]);
            else printf("%d %d %f\n", path[i]+1, i+1, cost[i]);
            sum += cost[i];
        }
    }
    printf("Total weight: %f\n", sum);
}

int find(int ds[], int f){
    int k = ds[f];
    int r = f;
    while (k > -1){
        r = k;
        k = ds[k];
    }
    return r;
}

void union_A(int ds[], int a, int b){
    ds[a] = b;
}

void kruskal(){
    BinHeap bh = CreateBinHeap();

    int n, m;
    scanf("%d%d", &n, &m);

    int disjointSet[n];

    for (int i = 0; i < n; i++) {
        disjointSet[i] = -1;
    }

    int b = n;
    int a = 10;
    while (b>=10){
        b/=10;
        a *= 10;
    }

    int u, v;
    float w;
    for (int i = 0; i < m; i++){
        scanf("%d%d%f", &u, &v, &w);
        u--;
        v--;
        insert(&bh, u*a + v, w);
    }

    int st;
    int en;
    float sum = 0;
    while (bh.size != 0){
        st = bh.A[0].data/a;
        en = bh.A[0].data%a;

        if (find(disjointSet, st) != find(disjointSet, en)){
            union_A(disjointSet, find(disjointSet, st), find(disjointSet, en));
            if (st < en) printf("%d %d %f\n", (st+1), (en+1), bh.A[0].priority);
            else printf("%d %d %f\n", (en+1), (st+1), bh.A[0].priority);
            sum += bh.A[0].priority;
        }
        pop(&bh);
    }
    printf("Total weight: %f\n", sum);
}

int main(int argc, char* argv[]) {

    if (strcmp(argv[1], "-p") == 0) prim();
    if (strcmp(argv[1], "-k") == 0) kruskal();

    return 0;
}
