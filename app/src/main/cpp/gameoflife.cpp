#include <jni.h>
#include <string.h>
#include <vector>

JNIEnv *globalEnv = NULL;


struct CellGrid {
    int rows;
    int cols;
    int size;
    bool* data;

    CellGrid(int rows_, int cols_) {
        rows = rows_;
        cols = cols_;
        size = rows * cols;
        data = new bool[size];
    }

    ~CellGrid() {
        delete[] data;
    }

    void setBooleanArray(const jbooleanArray& arr) {
        _LIBCPP_DEBUG_ASSERT(globalEnv != NULL, "ERROR: Env variable is NULL.");
        globalEnv->GetBooleanArrayRegion(arr, 0, size, reinterpret_cast<jboolean *>(data));
    }

    jbooleanArray getBooleanArray() {
        _LIBCPP_DEBUG_ASSERT(globalEnv != NULL, "ERROR: Env variable is NULL.");
        jbooleanArray boolArr = globalEnv->NewBooleanArray(size);
        globalEnv->SetBooleanArrayRegion(boolArr, 0, size, reinterpret_cast<const jboolean *>(data));
        return boolArr;
    }

    inline bool& get(int row, int col) {
        return data[(row * cols) + col];
    }

};


int wrap(int input, int ceiling) {
    if (input < ceiling) {
        if (input >= 0) {
            return input;
        }
        else {
            return ceiling + input;
        }
    }
    else {
        return input % ceiling;
    }
}



bool updateRule(CellGrid& oldGrid, int row, int col) {
    int countAlive = 0;
    for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
            if ((i != 0) || (j != 0)) {

                int r = wrap(row + i, oldGrid.rows);
                int c = wrap(col + j, oldGrid.cols);

                if (oldGrid.get(r, c)) {
                    countAlive++;
                }
            }
        }
    }

    bool thisCellIsOn = oldGrid.get(row, col);
    if (thisCellIsOn) {
        return (countAlive == 2 || countAlive == 3);
    }
    else {
        return (countAlive == 3);
    }
}

CellGrid updateGrid(CellGrid& oldGrid, int rows, int cols) {
    CellGrid newGrid(rows, cols);
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            newGrid.get(i, j) = updateRule(oldGrid, i, j);
        }
    }
    return newGrid;
}


extern "C"
JNIEXPORT jbooleanArray JNICALL
Java_com_example_gameoflife_grid_Grid_00024Companion_updateGrid(JNIEnv *env, jobject thiz,
                                                                jbooleanArray grid_data, jint rows,
                                                                jint cols) {
    globalEnv = env;
    CellGrid oldGrid(rows, cols);
    oldGrid.setBooleanArray(grid_data);
    return updateGrid(oldGrid, rows, cols).getBooleanArray();
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_example_gameoflife_grid_Grid_00024Companion_boolsToBytes(JNIEnv *env, jobject thiz,
                                                                  jbooleanArray bool_arr) {
    int size = env->GetArrayLength(bool_arr);
    void* arr = malloc(size);
    env->GetBooleanArrayRegion(bool_arr, 0, size, static_cast<jboolean *>(arr));

    jbyteArray byteArr = env->NewByteArray(size);
    env->SetByteArrayRegion(byteArr, 0, size, static_cast<const jbyte *>(arr));

    free(arr);

    return byteArr;
}

extern "C"
JNIEXPORT jbooleanArray JNICALL
Java_com_example_gameoflife_grid_Grid_00024Companion_bytesToBools(JNIEnv *env, jobject thiz,
                                                                  jbyteArray byte_arr) {
    int size = env->GetArrayLength(byte_arr);
    void* arr = malloc(size);
    env->GetByteArrayRegion(byte_arr, 0, size, static_cast<jbyte *>(arr));

    jbooleanArray boolArr = env->NewBooleanArray(size);
    env->SetBooleanArrayRegion(boolArr, 0, size, static_cast<const jboolean *>(arr));

    free(arr);

    return boolArr;
}
