export NDK=/Applications/adt-bundle-mac-x86_64/android-ndk-r10
export PREBUILT=$NDK/toolchains/arm-linux-androideabi-4.8/prebuilt/darwin-x86_64
export PLATFORM=$NDK/platforms/android-19/arch-arm/
export PATH=$PATH:$PREBUILT/bin
export TMPDIR=/workspace/dev/PlanA/AA/jni/FFmpeg/temp

./configure --target-os=linux \
--arch=arm \
--enable-version3 \
--enable-gpl \
--enable-nonfree \
--disable-stripping \
--disable-ffmpeg \
--disable-ffplay \
--disable-ffserver \
--disable-ffprobe \
--disable-encoders \
--disable-muxers \
--disable-devices \
--disable-protocols \
--enable-protocol=file \
--enable-avfilter \
--disable-network \
--disable-avdevice \
--enable-cross-compile \
--cc=$PREBUILT/bin/arm-linux-androideabi-gcc \
--cross-prefix=$PREBUILT/bin/arm-linux-androideabi- \
--nm=$PREBUILT/bin/arm-linux-androideabi-nm \
--extra-cflags="-fPIC -DANDROID" \
--disable-asm \
--enable-neon \
--enable-armv5te \
--extra-ldflags="-Wl,-T,$PREBUILT/arm-linux-androideabi/lib/ldscripts/armelf_linux_eabi.x -Wl,-rpath-link=$PLATFORM/usr/lib -L$PLATFORM/usr/lib -nostdlib $PREBUILT/lib/gcc/arm-linux-androideabi/4.8/crtbegin.o $PREBUILT/lib/gcc/arm-linux-androideabi/4.8/crtend.o -lc -lm -ldl"