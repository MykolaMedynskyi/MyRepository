#include <stdio.h>
#include <malloc.h>
#include <stdbool.h>
#include <stdlib.h>
#include <string.h>
#include <zconf.h>
#include <time.h>

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

void insert(BinHeap *bh, int data, int priority){
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

int main() {
    clock_t begin = clock();
    int n, m;
    scanf("%d%d", &n, &m);
    bool known[n];
    int path[n];
    float cost[n];
    float Ar[n][n];
    for (int i = 0; i < n; i++){
        for (int j = 0; j < n; j++){
            if (i == j) Ar[i][j] = 0;
            else Ar[i][j] = -1;
        }
        known[i] = false;
        path[i] = -1;
        cost[i] = INT_MAX;
    }

    int u, v;
    float w;
    for (int i = 0; i < m; i++){
        scanf("%d%d%f", &u, &v, &w);
        Ar[u-1][v-1] = w;
    }

    BinHeap bh = CreateBinHeap();
    for (int i = 0; i < n; i++){
        insert(&bh, i, INT_MAX);
    }

    int S;
    scanf("%d", &S);
    S = S-1;
    priority(&bh, S, 0);
    cost[S] = 0;

    while (bh.size != 0){
        known[bh.A[0].data] = true;
        for (int i = 0; i < n; i++) {
            if (Ar[bh.A[0].data][i] > 0){
                if (known[i] == false){
                    if (bh.A[0].priority != INT_MAX) {
                        if (cost[i] > Ar[bh.A[0].data][i] + bh.A[0].priority) {
                            priority(&bh, i, Ar[bh.A[0].data][i] + bh.A[0].priority);
                            path[i] = bh.A[0].data;
                            cost[i] = Ar[bh.A[0].data][i] + bh.A[0].priority;
                        }
                    }
                }
            }
        }
        pop(&bh);
    }

    for (int k = 0; k < n; k++) {
        if (cost[k] != INT_MAX) printf("go to %d: cost %f\n", k+1, cost[k]);
        else printf("go to %d: No path\n", k+1);
    }

    for (int k = 0; k < n; k++) {
        int p = 0;
        int l = k;
        fprintf(stderr, "%d", l+1);
        if (cost[k] == INT_MAX) fprintf(stderr, " no path");
        while (p!=-1){
            p = path[l];
            if (p != -1) fprintf(stderr, "  <-(+%f)  %d", Ar[p][l], p+1);
            l = p;
        }
        fprintf(stderr, "\n");
    }

    clock_t end = clock();
    double time_spent = (double)(end - begin) / CLOCKS_PER_SEC;
    fprintf(stderr, "time: %fms\n", time_spent/1000);

    return 0;
}