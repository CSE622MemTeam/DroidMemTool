#include "memutil.h"
#include <stdlib.h>

JNIEXPORT jboolean JNICALL Java_edu_buffalo_cse_memtool_MemUtil_malloc
  (JNIEnv *env, jclass class, jlong size)
{
  int i;
  char *buffer = malloc((size_t) size);
  if (buffer) for (i = 0; i < size; i++)
    buffer[i] = 'A';
  return !!buffer;
}
