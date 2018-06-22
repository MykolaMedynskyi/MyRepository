#include <stdio.h>
#include <malloc.h>
#include <stdbool.h>
#include <string.h>
#include <ctype.h>
#include <pcreposix.h>
#include <time.h>

//---------------------------------------------------BST

typedef struct BSTNode {
    char *data;
    struct BSTNode *left;
    struct BSTNode *right;
} BSTNode;

typedef struct BST {
    long size;
    long maxSize;
    BSTNode *root;
} BST;

BST* createBST(){
    BST *tmp = (BST*) malloc(sizeof(BST));
    tmp->size = 0;
    tmp->maxSize = 0;
    tmp->root = NULL;
    return tmp;
}

int alphaBetize (const char *a, const char *b) {
    int r = strcasecmp(a, b);
    if (r) return r;
    return -strcmp(a, b);
}

int strcasecmp (const char *a, const char *b) {
    while (*a && *b) {
        if (tolower(*a) != tolower(*b)) {
            break;
        }
        ++a;
        ++b;
    }
    return tolower(*a) - tolower(*b);
}

char *DataCheck(char *data){
    char line[255];
    int i = 0, j, last = 0, last2 = 0;
    strcpy(line, data);

    while (!((line[i] >= 'a' && line[i] <= 'z') || (line[i] >= 'A' && line[i] <= 'Z' || line[i]=='\0') || line[i]==' ')){
        for(j=i; line[j]!='\0'; ++j)
        {
            line[j] = line[j+1];
        }
        line[j] = '\0';
    }
    for (i=0; line[i]!='\0'; ++i){
        if ((line[i] >= 'a' && line[i] <= 'z') || (line[i] >= 'A' && line[i] <= 'Z')) last = i;
    }
    for (i=0; line[i]!=' '; ++i){
        if ((line[i] >= 'a' && line[i] <= 'z') || (line[i] >= 'A' && line[i] <= 'Z')) last2 = i;
    }
    if (last > last2) line[last2+1] = '\0';
    else line[last+1] = '\0';

    char *result = malloc(255);
    strcpy(result, line);
    return result;
}

void BSTInsert(BST *bst, char *data){
    data = DataCheck(data);
    if (strcmp(data, "")==0)
        return;
    BSTNode *tmp = (BSTNode*) malloc(sizeof(BSTNode));
    tmp->data = data;
    tmp->right = tmp->left = NULL;

    if (bst->size==0){
        bst->root = tmp;
        bst->size++;
        bst->maxSize++;
        return;
    }

    BSTNode *tmp2 = bst->root;
    while (true){
        if (alphaBetize(data, tmp2->data)>0){
            if (tmp2->right != NULL){
                tmp2 = tmp2->right;
            } else{
                tmp2->right = tmp;
                bst->size++;
                if (bst->size > bst->maxSize) bst->maxSize = bst->size;
                return;
            }
        } else{
            if (tmp2->left != NULL){
                tmp2 = tmp2->left;
            } else{
                tmp2->left = tmp;
                bst->size++;
                if (bst->size > bst->maxSize) bst->maxSize = bst->size;
                return;
            }
        }
    }

}

void DoBSTInorder(BSTNode *node){
    if (node == NULL) return;
    DoBSTInorder(node->left);
    printf("%s ", node->data);
    DoBSTInorder(node->right);
}

void BSTInorder(BST *bst){
    if (bst->root == NULL) return;
    BSTNode *tmp = bst->root;
    DoBSTInorder(tmp);
    printf("\n");
}

BSTNode* DoBSTmin(BSTNode *node){
    BSTNode *tmp = node;
    while (tmp->left!=NULL) tmp = tmp->left;
    return tmp;
}

void BSTmin(BST *bst){
    if (bst->root == NULL) return;
    printf("%s\n", DoBSTmin(bst->root)->data);
}

BSTNode* DoBSTmax(BSTNode *node){
    BSTNode *tmp = node;
    while (tmp->right!=NULL) tmp = tmp->right;
    return tmp;
}

void BSTmax(BST *bst){
    if (bst->root == NULL) return;
    printf("%s\n", DoBSTmax(bst->root)->data);
}

BSTNode* DoBSTfind(BST *bst, char* data){
    BSTNode *tmp = bst->root;
    while (true){
        if (alphaBetize(data, tmp->data)>0){
            if (tmp->right != NULL){
                tmp = tmp->right;
            } else{
                return NULL;
            }
        } else if (alphaBetize(data, tmp->data)<0) {
            if (tmp->left != NULL){
                tmp = tmp->left;
            } else{
                return NULL;
            }
        } else {
            return tmp;
        }

    }
}

void BSTfind(BST *bst, char* data){
    if (bst->root == NULL) return;
    BSTNode *tmp = DoBSTfind(bst, data);
    if (tmp==NULL) {
        printf("%d\n", 0);
        return;
    }
    printf("%d\n", 1);
}

BSTNode* DoBSTSuccessor(BST *bst, char* data){
    if (bst == NULL){
        return NULL;
    }

    BSTNode *tmp = bst->root;
    BSTNode *successor = NULL;

    if ((DoBSTfind(bst, data) == NULL) || (strcmp(DoBSTmax(bst->root)->data, data) == 0)) {
        return NULL;
    }

    while (true){
        if (strcmp(tmp->data, data) == 0){
            if (tmp->right != NULL){
                tmp = tmp->right;
                while (tmp->left != NULL){
                    tmp = tmp->left;
                }
                return tmp;
            } else{
                return successor;
            }
        } else {
            if (alphaBetize(data, tmp->data)>0){
               tmp = tmp->right;
            } else{
                successor = tmp;
                tmp = tmp->left;
            }
        }
    }
}

void BSTSuccessor(BST *bst, char* data){
    BSTNode *tmp = DoBSTSuccessor(bst, data);
    if (tmp == NULL) printf("\n");
    else printf("%s\n", tmp->data);
}

void BSTdelete(BST *bst, char *data){
    if (bst->root == NULL) return;
    if (DoBSTfind(bst, data) == NULL) return;
    BSTNode *tmp = bst->root;
    BSTNode *toDel = DoBSTfind(bst, data);
    BSTNode *father = NULL;
    char* remember = NULL;
    bool right = true;
    if ((strcmp(bst->root->data, data) != 0) && ((toDel->right == NULL) || (toDel->left == NULL))){
        while (father == NULL) {
            if (alphaBetize(data, tmp->data) > 0) {
                if (strcmp(tmp->right->data, data) == 0){
                    father = tmp;
                } else{
                    tmp = tmp->right;
                }
            } else{
                if (strcmp(tmp->left->data, data) == 0){
                    father = tmp;
                    right = false;
                } else{
                    tmp = tmp->left;
                }
            }
        }
    }

    if ((toDel->left != NULL) && (toDel->right != NULL)){
        remember = (DoBSTmin(toDel->right)->data);
        BSTdelete(bst, remember);
    } else if ((toDel->left == NULL) && (toDel->right == NULL)){
        if (father == NULL){
            bst->root = NULL;
        } else{
            if (right) father->right = NULL;
            else father->left = NULL;
        }
        bst->size--;
        free(toDel);
    } else{
        if (father == NULL){
            if (toDel->left == NULL) bst->root = toDel->right;
            else bst->root = toDel->left;
        } else{
            if (toDel->left == NULL){
                if (right) father->right = toDel->right;
                else father->left = toDel->right;
            } else{
                if (right) father->right = toDel->left;
                else father->left = toDel->left;
            }
        }
        bst->size--;
        free(toDel);
    }

    if (remember != NULL) toDel->data = remember;
}

void loadBST (BST *bst, char *file) {
    char x[1024];
    FILE *f = fopen(file, "r");
    while (fscanf(f, " %1023s", x) == 1) {
        BSTInsert(bst, x);
    }
}

//--------------------------------------------------------RBT

typedef struct RBTNode {
    char *data;
    bool black;
    struct RBTNode *left;
    struct RBTNode *right;
    struct RBTNode *father;
} RBTNode;


typedef struct RBT {
    long size;
    long maxSize;
    RBTNode *root;
} RBT;

RBT* createRBT(){
    RBT *tmp = (RBT*) malloc(sizeof(RBT));
    tmp->size = 0;
    tmp->maxSize = 0;
    tmp->root = NULL;
    return tmp;
}

RBTNode* uncle(RBTNode *n)
{
    RBTNode *g = n->father->father;
    if (g == NULL)
        return NULL;
    if (n->father == g->left)
        return g->right;
    else
        return g->left;
}

void RBTLeftRotation(RBT *rbt, RBTNode *node){
    RBTNode *right = node->right;
    RBTNode *bro = node->right->left;
    if (node->father != NULL){
        if (node->father->left == node){
            node->father->left = right;
        } else{
            node->father->right = right;
        }
        right->father = node->father;
        node->father = right;
    } else{
        rbt->root = right;
        right->father = NULL;
        node->father = right;
    }
    if (bro != NULL) bro->father = node;
    node->right = bro;
    right->left = node;
}

void RBTRightRotation(RBT *rbt, RBTNode *node){
    RBTNode *left = node->left;
    RBTNode *bro = node->left->right;
    if (node->father != NULL){
        if (node->father->left == node){
            node->father->left = left;
        } else{
            node->father->right = left;
        }
        left->father = node->father;
        node->father = left;
    } else{
        rbt->root = left;
        left->father = NULL;
        node->father = left;
    }
    if (bro != NULL) bro->father = node;
    node->left = bro;
    left->right = node;
}

void RBTInsertCase1(RBT *rbt, RBTNode *node);
void RBTInsertCase2(RBT *rbt, RBTNode *node);
void RBTInsertCase3(RBT *rbt, RBTNode *node);
void RBTInsertCase4(RBT *rbt, RBTNode *node);
void RBTInsertCase5(RBT *rbt, RBTNode *node);

void RBTdeleteCase1(RBT *rbt, RBTNode *node);
void RBTdeleteCase2(RBT *rbt, RBTNode *node);
void RBTdeleteCase3(RBT *rbt, RBTNode *node);
void RBTdeleteCase4(RBTNode *node);

void RBTInsertCase5(RBT *rbt, RBTNode *node){
    RBTNode *g = node->father->father;
    node->father->black = true;
    g->black = false;
    if ((node == node->father->left) && (node->father == g->left)) {
        RBTRightRotation(rbt, g);
    } else
        RBTLeftRotation(rbt, g);
}

void RBTInsertCase4(RBT *rbt, RBTNode *node){
    RBTNode *g = node->father->father;
    if (node->father->right == node && g->left == node->father){
        RBTLeftRotation(rbt, node->father);
        node = node->left;
    } else if (node->father->left == node && g->right == node->father){
        RBTRightRotation(rbt, node->father);
        node = node->right;
    }
    RBTInsertCase5(rbt, node);
}

void RBTInsertCase3(RBT *rbt, RBTNode *node){
    RBTNode *u = uncle(node);
    RBTNode *g;
    if (u != NULL && u->black == false){
        g = node->father->father;
        node->father->black = true;
        u->black = true;
        g->black = false;
        RBTInsertCase1(rbt, g);
    } else
        RBTInsertCase4(rbt, node);
}

void RBTInsertCase2(RBT *rbt, RBTNode *node){
    if (node->father->black == true) return;
    else RBTInsertCase3(rbt, node);
}

void RBTInsertCase1(RBT *rbt, RBTNode *node){
    if (node->father == NULL)
        node->black = true;
    else
        RBTInsertCase2(rbt, node);
}

void RBTInsert(RBT *rbt, char *data){
    data = DataCheck(data);
    if (strcmp(data, "")==0)
        return;
    RBTNode *tmp = (RBTNode*) malloc(sizeof(RBTNode));
    tmp->data = data;
    tmp->left =  NULL;
    tmp->right = NULL;
    tmp->father = NULL;
    tmp->black = false;

    if (rbt->size == 0) {
        tmp->black = true;
        rbt->root = tmp;
        rbt->size = 1;
        rbt->maxSize = 1;
        return;
    }


    RBTNode *tmp2 = rbt->root;

    while (true){
        if (alphaBetize(data, tmp2->data)>0){
            if (tmp2->right != NULL){
                tmp2 = tmp2->right;
            } else{
                tmp->father = tmp2;
                tmp2->right = tmp;
                rbt->size++;
                if (rbt->size > rbt->maxSize) rbt->maxSize = rbt->size;
                RBTInsertCase2(rbt, tmp);
                return;
            }
        } else{
            if (tmp2->left != NULL){
                tmp2 = tmp2->left;
            } else{
                tmp2->left = tmp;
                tmp->father = tmp2;
                rbt->size++;
                if (rbt->size > rbt->maxSize) rbt->maxSize = rbt->size;
                RBTInsertCase2(rbt, tmp);
                return;
            }
        }
    }
}

void DoRBTInorder(RBTNode *node){
    if (node == NULL) return;
    DoRBTInorder(node->left);
    printf("%s ", node->data);
    DoRBTInorder(node->right);
}

void RBTInorder(RBT *rbt){
    if (rbt->root == NULL) return;
    RBTNode *tmp = rbt->root;
    DoRBTInorder(tmp);
    printf("\n");
}

RBTNode* DoRBTmin(RBTNode *node){
    RBTNode *tmp = node;
    while (tmp->left!=NULL) tmp = tmp->left;
    return tmp;
}

void RBTmin(RBT *rbt){
    if (rbt->root == NULL) return;
    printf("%s\n", DoRBTmin(rbt->root)->data);
}

RBTNode* DoRBTmax(RBTNode *node){
    RBTNode *tmp = node;
    while (tmp->right!=NULL) tmp = tmp->right;
    return tmp;
}

void RBTmax(RBT *rbt){
    if (rbt->root == NULL) return;
    printf("%s\n", DoRBTmax(rbt->root)->data);
}

RBTNode* DoRBTfind(RBT *rbt, char* data){
    RBTNode *tmp = rbt->root;
    while (true){
        if (alphaBetize(data, tmp->data)>0){
            if (tmp->right != NULL){
                tmp = tmp->right;
            } else{
                return NULL;
            }
        } else if (alphaBetize(data, tmp->data)<0) {
            if (tmp->left != NULL){
                tmp = tmp->left;
            } else{
                return NULL;
            }
        } else {
            return tmp;
        }

    }
}

void RBTfind(RBT *rbt, char* data){
    if (rbt->root == NULL) return;
    RBTNode *tmp = DoRBTfind(rbt, data);
    if (tmp==NULL) {
        printf("%d\n", 0);
        return;
    }
    printf("%d\n", 1);
}

RBTNode* DoRBTSuccessor(RBT *rbt, char* data){
    if (rbt == NULL){
        return NULL;
    }

    RBTNode *tmp = rbt->root;
    RBTNode *successor = NULL;

    if ((DoRBTfind(rbt, data) == NULL) || (strcmp(DoRBTmax(rbt->root)->data, data) == 0)) {
        return NULL;
    }

    while (true){
        if (strcmp(tmp->data, data) == 0){
            if (tmp->right != NULL){
                tmp = tmp->right;
                while (tmp->left != NULL){
                    tmp = tmp->left;
                }
                return tmp;
            } else{
                return successor;
            }
        } else {
            if (alphaBetize(data, tmp->data)>0){
                tmp = tmp->right;
            } else{
                successor = tmp;
                tmp = tmp->left;
            }
        }
    }
}

void RBTSuccessor(RBT *rbt, char* data){
    RBTNode *tmp = DoRBTSuccessor(rbt, data);
    if (tmp == NULL) printf("\n");
    else printf("%s\n", tmp->data);
}

void loadRBT (RBT *rbt, char *file) {
    char x[1024];
    FILE *f = fopen(file, "r");
    while (fscanf(f, " %1023s", x) == 1) {
        RBTInsert(rbt, x);
    }
}

void RBTdeleteCase4(RBTNode *node){
    if (node->right == NULL && node->left != NULL){
        if (node->father->left == node){
            node->father->left = node->left;
        } else{
            node->father->right = node->left;
        }
        node->left->father = node->father;
        node->left->black = true;
    } else{
        if (node->father->left == node){
            node->father->left = node->right;
        } else{
            node->father->right = node->right;
        }
        node->right->father = node->father;
        node->right->black = true;
    }
    free(node);
}

void RBT2Bcheck(RBT *rbt, RBTNode *node){
    RBTNode *bro = NULL;
    if (node->father->left == node)
        bro = node->father->right;
    else
        bro = node->father->left;

    RBTNode *nephewL = NULL;
    RBTNode *nephewR = NULL;
    bool nepLblack = false;
    bool nepRblack = false;
    if (bro->left != NULL){
        nephewL = bro->left;
        nepLblack = nephewL->black;
    }
    if (bro->right != NULL){
        nephewR = bro->right;
        nepRblack = nephewR->black;
    }
    if (nephewL == NULL) nepLblack = true;
    if (nephewR == NULL) nepRblack = true;
    if (bro->black && nepLblack && nepRblack){
        bool b = node->father->black;
        bro->black = false;
        node->father->black = true;
        if (b) RBT2Bcheck(rbt, node->father);
    } else if (!(bro->black)){
        node->father->black = false;
        bro->black = true;
        if (node->father->left == node){
            RBTLeftRotation(rbt, node->father);
        } else{
            RBTRightRotation(rbt, node->father);
        }
        RBT2Bcheck(rbt, node->father);
    } else if (bro->black && !nepRblack && !nepLblack){
        if (node->father->left == node){
            bro->right->black = true;
            RBTLeftRotation(rbt, node->father);
        } else{
            bro->left->black = true;
            RBTRightRotation(rbt, node->father);
        }
    } else{
        if (node->father->left == node){
            if (nepLblack){
                bro->right->black = true;
                bro->left->black = true;
                RBTLeftRotation(rbt, node->father);
            } else{
                bro->left->black = true;
                bro->black = false;
                RBTRightRotation(rbt, bro);
                RBT2Bcheck(rbt, node->father);
            }
        } else{
            if (nepRblack){
                bro->left->black = true;
                bro->right->black = true;
                RBTRightRotation(rbt, node->father);
            } else{
                bro->right->black = true;
                bro->black = false;
                RBTLeftRotation(rbt, bro);
                RBT2Bcheck(rbt, node->father);
            }
        }
    }
}

void RBTdeleteCase3(RBT *rbt, RBTNode *node){
    if (node->left == NULL && node->right == NULL){
        if (!(node->black)){
            if (node->father->left == node){
                node->father->left = NULL;
            } else
                node->father->right = NULL;
            free(node);
        } else{
            RBT2Bcheck(rbt, node);
            rbt->root->black = true;
            if (node->father->left == node){
                node->father->left = NULL;
            } else
                node->father->right = NULL;
            free(node);
        }
    } else{
        RBTdeleteCase4(node);
    }
}

void RBTdeleteCase2(RBT *rbt, RBTNode *node) {
    if (node->left != NULL && node->right != NULL){
        RBTNode *toDel = DoRBTmin(node->right);
        node->data = toDel->data;
        RBTdeleteCase3(rbt, toDel);
    } else
        RBTdeleteCase3(rbt, node);
}

void RBTdeleteCase1(RBT *rbt, RBTNode *node){
    if (rbt->size == 1){
        rbt->root = NULL;
        free(node);
    } else
        RBTdeleteCase2(rbt, node);
}

void RBTdelete(RBT *rbt, char *data) {
    if (rbt->root == NULL) return;
    if (DoRBTfind(rbt, data) == NULL) return;
    RBTNode *toDel = DoRBTfind(rbt, data);
    RBTdeleteCase1(rbt, toDel);
    rbt->size--;
}

//--------------------------------------------------------HMap

typedef struct ListNode {
    char *data;
    long key;
    struct ListNode *next;
    struct ListNode *prev;
} ListNode;

typedef struct List {
    int size;
    struct ListNode *head;
} List;

typedef struct HMAPNode {
    int number;
    int size;
    struct List *list;
    struct RBT *rbt;
    struct HMAPNode *next;
    struct HMAPNode *prev;
} HMAPNode;


typedef struct Hmap {
    HMAPNode *root;
    long size;
    long maxSize;
} Hmap;

long hash(char *str){   //djb2 by Dan Bernstein
    long hash = 5381;
    int a = 0;
    while (*str++) {
        int c = *str;
        a++;
        if (a == 10)
            return hash;
        hash = (hash << 5) + hash + c; /* hash * 33 + c */
    }
    return hash;
}

List* createList() {
    List *tmp = (List*) malloc(sizeof(List));
    tmp->size = 0;
    tmp->head = NULL;
    return tmp;
}

void AddNodeHmap(Hmap *hmap, int n) {
    HMAPNode *tmp = (HMAPNode*) malloc(sizeof(HMAPNode));
    tmp->number = n;
    tmp->list = createList();
    tmp->rbt = NULL;
    tmp->size = 0;
    if (n == 0) {
        hmap->root = tmp;
        tmp->next = tmp;
        tmp->prev = tmp;
    }else {
        tmp->next = hmap->root;
        tmp->prev = hmap->root->prev;
        hmap->root->prev->next = tmp;
        hmap->root->prev = tmp;
    }
}


Hmap* createHmap(){
    Hmap *tmp = (Hmap*) malloc(sizeof(Hmap));
    int i;
    for(i = 0; i < 13; i++){
        AddNodeHmap(tmp, i);
    }
    return tmp;
}

void deleteList(List **list) {
    ListNode *tmp = (*list)->head;
    ListNode *next = NULL;
    int n = 0;
    while (n<(*list)->size) {
        next = tmp->next;
        free(tmp);
        tmp = next;
        n++;
    }
    free(*list);
    (*list) = NULL;
}

void pushFront(List *list, char *data, long key) {
    ListNode *tmp = (ListNode*) malloc(sizeof(ListNode));
    tmp->data = data;
    tmp->key = key;
    if (list->size == 0) {
        list->head = tmp;
        tmp->next = tmp;
        tmp->prev = tmp;
    }else {
        tmp->next = list->head;
        tmp->prev = list->head->prev;
        list->head = tmp;
        list->head->next->prev = tmp;
        list->head->prev->next = tmp;
    }
    list->size++;
}

HMAPNode *findHmapNode(Hmap *hmap, long b){
    HMAPNode *tmp = hmap->root;
    if (b == 0) return tmp;
    if (b <= 7)
        while (tmp->number != b) tmp = tmp->next;
    else
        while (tmp->number != b) tmp = tmp->prev;
    return tmp;
}

void ListToTree(HMAPNode *hnode){
    ListNode *tmp = hnode->list->head;
    hnode->rbt = createRBT();
    for (int i = 0; i<100; i++){
        RBTInsert(hnode->rbt, tmp->data);
        tmp = tmp->next;
    }
    deleteList(&(hnode->list));
    hnode->list = NULL;
}

void HMAPinsert(Hmap *hmap, char *data){
    data = DataCheck(data);
    if (strcmp(data, "")==0)
        return;
    long key = hash(data);
    long bucket = key % 13;
    HMAPNode *hnode = findHmapNode(hmap, bucket);
    bool tree = false;
    if (hnode->rbt != NULL) tree = true;
    if (hnode->size < 100 && !tree){
        pushFront(hnode->list, data, key);
    } else if (hnode->size == 100 && !tree){
        ListToTree(hnode);
        RBTInsert(hnode->rbt, data);
    } else RBTInsert(hnode->rbt, data);

    hnode->size++;
    hmap->size++;
    if (hmap->size > hmap->maxSize) hmap->maxSize = hmap->size;
}

ListNode *ListFind(List *list, char *data){
    ListNode *tmp = list->head;
    for (int i = 0; i < list->size; i++){
        if (strcmp(data, tmp->data) != 0) tmp = tmp->next;
        else return tmp;
    }
    return NULL;
}

int DoHmapFind(Hmap *hmap, char *data){
    long key = hash(data);
    long bucket = key % 13;
    HMAPNode *hnode = findHmapNode(hmap, bucket);
    if (hnode->size == 0) return 0;
    if (hnode->rbt != NULL){
        RBTNode *tmp = DoRBTfind(hnode->rbt, data);
        if (tmp == NULL) return 0;
        else return 1;
    } else{
        ListNode *list = ListFind(hnode->list, data);
        if (list == NULL) return 0;
        else return 1;
    }
}

void HmapFind(Hmap *hmap, char *data){
    int result = DoHmapFind(hmap, data);
    printf ("%d\n", result);
}

void deleteListNode(HMAPNode *hnode, char *data){
    ListNode *tmp = ListFind(hnode->list, data);
    if (tmp == NULL) return;
    if (tmp->next == tmp){
        hnode->list->head = NULL;
        free(tmp);
        hnode->list->size = 0;
        return;
    }
    tmp->prev->next = tmp->next;
    tmp->next->prev = tmp->prev;
    if (hnode->list->head == tmp) hnode->list->head = tmp->next;
    free(tmp);
    hnode->list->size--;
}

void HmapDelete(Hmap *hmap, char *data){
    long key = hash(data);
    long bucket = key % 13;
    HMAPNode *hnode = findHmapNode(hmap, bucket);
    if (hnode->size == 0) return;
    if (hnode->list != NULL){
        deleteListNode(hnode, data);
    } else{
        RBTdelete(hnode->rbt, data);
    }
    hnode->size--;
    hmap->size--;
}

void LoadHmap(Hmap *hmap, char *file) {
    char x[1024];
    FILE *f = fopen(file, "r");
    while (fscanf(f, " %1023s", x) == 1) {
        HMAPinsert(hmap, x);
    }
}

//--------------------------------------------main

void main(int argc, char *argv[]) {
    if (strcmp(argv[1], "--type") != 0){
        printf("error\n");
        return;
    }
    if ((strcmp(argv[2], "rbt")) != 0 && (strcmp(argv[2], "bst") != 0) && (strcmp(argv[2], "hmap") != 0)){
        printf("error tut\n");
        return;
    }
    int j = 0;

    char *n;
    char buf[512];
    scanf("%511s",buf);
    long int N = strtol(buf, &n, 10);
    char **operations = (char**)malloc(N * sizeof(char*));
    char **data = (char**)malloc(N * sizeof(char*));

    for(int i = 0; i < N; ++i)
    {
        scanf("%511s",buf);
        operations[i] = malloc(strlen(buf+1));
        strcpy(operations[i], buf);
        if (!((strcmp(operations[i], "min") == 0) || (strcmp(operations[i], "max") == 0) ||
            (strcmp(operations[i], "inorder") == 0))){
            scanf("%511s",buf);
            data[j] = malloc(strlen(buf+1));
            strcpy(data[j], buf);
            j++;
        }
    }
    j = 0;
    clock_t begin = clock();
    if (strcmp(argv[2], "bst") == 0){
        BST *bst = createBST();
        for (int i=0; i<N; i++){
            int Case = 0;
            if (strcmp(operations[i], "insert") == 0) Case = 1;
            if (strcmp(operations[i], "load") == 0) Case = 2;
            if (strcmp(operations[i], "delete") == 0) Case = 3;
            if (strcmp(operations[i], "find") == 0) Case = 4;
            if (strcmp(operations[i], "min") == 0) Case = 5;
            if (strcmp(operations[i], "max") == 0) Case = 6;
            if (strcmp(operations[i], "successor") == 0) Case = 7;
            if (strcmp(operations[i], "inorder") == 0) Case = 8;
            switch (Case){
                case 1:{
                    BSTInsert(bst, data[j]);
                    j++;
                    break;
                }
                case 2:{
                    loadBST(bst, data[j]);
                    j++;
                    break;
                }
                case 3:{
                    BSTdelete(bst, data[j]);
                    j++;
                    break;
                }
                case 4:{
                    BSTfind(bst, data[j]);
                    j++;
                    break;
                }
                case 5:{
                    BSTmin(bst);
                    break;
                }
                case 6:{
                    BSTmax(bst);
                    break;
                }
                case 7:{
                    BSTSuccessor(bst, data[j]);
                    j++;
                    break;
                }
                case 8:{
                    BSTInorder(bst);
                    break;
                }
                default:
                    break;
            }
        }
        fprintf(stderr, "elements in bst: %lu\nmax elements was: %lu\n", bst->size, bst->maxSize);
    }

    if (strcmp(argv[2], "rbt") == 0){
        RBT *rbt = createRBT();
        for (int i=0; i<N; i++){
            int Case = 0;
            if (strcmp(operations[i], "insert") == 0) Case = 1;
            if (strcmp(operations[i], "load") == 0) Case = 2;
            if (strcmp(operations[i], "delete") == 0) Case = 3;
            if (strcmp(operations[i], "find") == 0) Case = 4;
            if (strcmp(operations[i], "min") == 0) Case = 5;
            if (strcmp(operations[i], "max") == 0) Case = 6;
            if (strcmp(operations[i], "successor") == 0) Case = 7;
            if (strcmp(operations[i], "inorder") == 0) Case = 8;
            switch (Case){
                case 1:{
                    RBTInsert(rbt, data[j]);
                    j++;
                    break;
                }
                case 2:{
                    loadRBT(rbt, data[j]);
                    j++;
                    break;
                }
                case 3:{
                    RBTdelete(rbt, data[j]);
                    j++;
                    break;
                }
                case 4:{
                    RBTfind(rbt, data[j]);
                    j++;
                    break;
                }
                case 5:{
                    RBTmin(rbt);
                    break;
                }
                case 6:{
                    RBTmax(rbt);
                    break;
                }
                case 7:{
                    RBTSuccessor(rbt, data[j]);
                    j++;
                    break;
                }
                case 8:{
                    RBTInorder(rbt);
                    break;
                }
                default:
                    break;
            }
        }
        fprintf(stderr, "elements in rbt: %lu\nmax elements was: %lu\n", rbt->size, rbt->maxSize);
    }

    if (strcmp(argv[2], "hmap") == 0){
        Hmap *hmap = createHmap();
        for (int i=0; i<N; i++){
            int Case = 0;
            if (strcmp(operations[i], "insert") == 0) Case = 1;
            if (strcmp(operations[i], "load") == 0) Case = 2;
            if (strcmp(operations[i], "delete") == 0) Case = 3;
            if (strcmp(operations[i], "find") == 0) Case = 4;
            if (strcmp(operations[i], "min") == 0) Case = 5;
            if (strcmp(operations[i], "max") == 0) Case = 6;
            if (strcmp(operations[i], "successor") == 0) Case = 7;
            if (strcmp(operations[i], "inorder") == 0) Case = 8;
            switch (Case){
                case 0:{
                    break;
                }
                case 1:{
                    HMAPinsert(hmap, data[j]);
                    j++;
                    break;
                }
                case 2:{
                    LoadHmap(hmap, data[j]);
                    j++;
                    break;
                }
                case 3:{
                    HmapDelete(hmap, data[j]);
                    j++;
                    break;
                }
                case 4:{
                    HmapFind(hmap, data[j]);
                    j++;
                    break;
                }
                case 5:{
                    printf("\n");
                    break;
                }
                case 6:{
                    printf("\n");
                    break;
                }
                case 7:{
                    printf("\n");
                    j++;
                    break;
                }
                case 8:{
                    printf("\n");
                    break;
                }
                default:
                    break;
            }
        }
        fprintf(stderr, "elements in hmap: %lu\nmax elements was: %lu\n", hmap->size, hmap->maxSize);
    }

    clock_t end = clock();
    double time_spent = (double)(end - begin) / CLOCKS_PER_SEC;
    fprintf(stderr, "time: %f\n", time_spent);
}