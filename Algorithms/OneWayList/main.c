#include <stdio.h>
#include <stdlib.h>
#include <time.h>


typedef struct Node {
    int value;
    struct Node *next;
} Node;

typedef struct List {
    int size;
    Node *head;
} List;

List* createList() {
    List *tmp = (List*) malloc(sizeof(List));
    tmp->size = 0;
    tmp->head = NULL;
    return tmp;
}

void push(List *list, int data) {
    Node *tmp = (Node*) malloc(sizeof(Node));
    tmp->value = data;
    tmp->next = list->head;
    list->head = tmp;
    //printf("Add %d\n", data);
    list->size++;
}

int pop(List *list){
    if (list->head == NULL){
        exit(-1);
    }

    Node* toDelete = NULL;
    toDelete = list->head;
    int whatD = toDelete->value;
    list->head = list->head->next;
    free(toDelete);
    list->size--;
    return whatD;
}


Node* getN(List *list, int n) {
    int counter = 0;
    Node *tmp =list->head;
    while (counter < n && tmp) {
        tmp = tmp->next;
        counter++;
    }
    return list->head;
}


Node* getLast(List *list) {
    Node *tmp = list->head;
    if (list->head == NULL) {
        return NULL;
    }
    while (tmp->next) {
        tmp = tmp->next;
    }
    return list->head;
}

void pushBack(List list, int data){
    Node *last = getLast(&list);
    Node *tmp = (Node*) malloc(sizeof(Node));
    tmp->value = data;
    tmp->next = NULL;
    last->next = tmp;
    list.size++;
}

int popBack(List *list){
    Node *PresentNode = NULL;
    Node *PreviousNode = NULL;
    if (!(list->head)) {
        exit(-1);
    }
    PresentNode = list->head;
    while(PresentNode->next){
        PreviousNode = PresentNode;
        PresentNode = PresentNode->next;
    }
    int whatD = PresentNode->value;
    if (PreviousNode == NULL) {
        free(list->head);
        list->head = NULL;
    }
    else {
        PreviousNode->next = NULL;
        free(PresentNode);
    }
    list->size--;
    return whatD;
}

void insert(List *list, int n, int value){
    int i = 1;
    if (n==0){
        push(list, 0);
        return;
    }
    Node *tmp = (Node*) malloc(sizeof(Node));
    while(n>i && list->head->next){
        list->head = list->head->next;
        i++;
    }
    tmp->value = value;
    if (list->head->next) {
        tmp->next = list->head->next;
    } else tmp->next = NULL;

    list->head->next = tmp;
    list->size++;
}

int deleteN(List *list, int n){
    if (n == 0) {
        return pop(list);
    }else{
        Node *prev = getN(list, n-1);
        Node *el = prev->next;
        prev->next = el->next;
        int whatD = el->value;
        free(el);
        list->size--;
        return whatD;
    }
}

void deleteList(List *list){
    while (list->head->next) {
        pop(list);
        list->head = list->head->next;
    }
    free(list);
}


void printList(const List *list) {
    Node *tmp = list->head;
    while (tmp) {
        printf("%d ", tmp->value);
        tmp = tmp->next;
    }
    printf("\n");
}


void fromArray(List *list, int *arr, size_t size) {
    size_t i = size - 1;
    if (arr == NULL || size == 0) {
        return;
    }
    do {
        push(list, arr[i]);
    } while(i--!=0);
}

double timeToN(List *list, int a){
    clock_t begin = clock();
    int j = 0;
    Node *tmp = list->head;
    for (int i=0; i<1000000; i++){
        while(a>j){
            tmp = tmp->next;
            j++;
        }
        j=0;
        tmp = list->head;
    }
    clock_t end = clock();
    double time_spent = (double)(end - begin) / CLOCKS_PER_SEC;
    return time_spent;
}

double timeToRandom(List *list, int a[]){
    clock_t begin = clock();
    int j = 0;
    Node *tmp = list->head;
    for (int i=0; i<1000; i++){
        for (int k=0; k<1000; k++) {
            while (a[i] > j) {
                tmp = tmp->next;
                j++;
            }
            j = 0;
            tmp = list->head;
        }
    }
    clock_t end = clock();
    double time_spent = (double)(end - begin) / CLOCKS_PER_SEC;
    return time_spent;
}

int getPos(List *list, int value){
    Node *tmp = list->head;
    int n = 0;
    while (n<list->size){
        if (tmp->value==value){
            return n;
        }
        tmp=tmp->next;
        n++;
    }
    return -1;
}

List* merge(List *list, List *list2){
    Node *tmp2 = list2->head;
    List *tmp = list;
    for (int i = list2->size; i>0; i--){
        push(tmp, tmp2->value);
        tmp2=tmp2->next;
    }
    return tmp;
}


int main() {
    List *list = createList();
    srand(time(NULL));
    int Arr[1000];
    int ArrOfNtoR[1000];
    int a = rand()%1000;
    for (int i = 0; i<1000; i++){
        Arr[i] = rand()%10000;
        ArrOfNtoR[i] = rand()%1000;
    }
    fromArray(list, Arr, 1000);
    printf ("to element %d  1m times get %f seconds\n", a, timeToN(list, a));
    printf ("to random element  1m times get %f seconds\n", timeToRandom(list, ArrOfNtoR));
    printf ("in the %d position value is: %d\n", a, getN(list, a)->value);
    printf("The el. %d in position %d",getN(list, a)->value, getPos(list, getN(list, a)->value));

    return 0;
}