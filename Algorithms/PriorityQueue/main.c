#include <stdio.h>
#include <malloc.h>
#include <stdbool.h>
#include <stdlib.h>
#include <string.h>

#define LCHILD(x) 2 * x + 1
#define RCHILD(x) 2 * x + 2
#define PARENT(x) (x - 1) / 2


typedef struct node {
    int priority;
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
    if (bh->size == 0){
        printf("\n");
        return;
    }
    top(*bh);
    bh->A[0] = bh->A[--(bh->size)] ;
    bh->A = realloc(bh->A, bh->size * sizeof(node)) ;
    heapify(bh, 0) ;
}

void print(BinHeap *bh){
    int i ;
    for(i = 0; i < bh->size; i++) {
        printf("(%d %d) ", bh->A[i].data, bh->A[i].priority) ;
    }
    printf("\n");
}

void priority(BinHeap *bh, int data, int newPriority){
    long i = 0, k;
    while (i < bh->size){
        if (bh->A[i].data == data){
            printf("sosi%d \n", newPriority);
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
    BinHeap bh = CreateBinHeap();

    char *n;
    char buf[512];
    scanf("%511s",buf);
    long int N = strtol(buf, &n, 10);
    char **operations = (char**)malloc(N * sizeof(char*));
    int data[2*N];

    int j = 0;
    for(int i = 0; i < N; ++i)
    {
        scanf("%511s",buf);
        operations[i] = malloc(strlen(buf+1));
        strcpy(operations[i], buf);
        if ((strcmp(operations[i], "insert") == 0) || (strcmp(operations[i], "priority") == 0)){
            scanf("%d",&data[j]);
            scanf("%d",&data[j+1]);
            j+=2;
        }
    }
    j = 0;
    for (int i=0; i<N; i++){
        int Case = 0;
        if (strcmp(operations[i], "insert") == 0) Case = 1;
        if (strcmp(operations[i], "pop") == 0) Case = 2;
        if (strcmp(operations[i], "empty") == 0) Case = 3;
        if (strcmp(operations[i], "top") == 0) Case = 4;
        if (strcmp(operations[i], "priority") == 0) Case = 5;
        if (strcmp(operations[i], "print") == 0) Case = 6;
        switch (Case){
            case 1:{
                insert(&bh, data[j], data[j+1]);
                j+=2;
                break;
            }
            case 2:{
                pop(&bh);
                break;
            }
            case 3:{
                empty(bh);
                break;
            }
            case 4:{
                top(bh);
                break;
            }
            case 5:{
                priority(&bh, data[j], data[j+1]);
                j+=2;
                break;
            }
            case 6:{
                print(&bh);
                break;
            }
            default:
                break;
        }
    }

    return 0;
}