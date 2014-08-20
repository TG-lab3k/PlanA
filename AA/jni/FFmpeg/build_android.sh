#!/bin/bash
######################################################
# Usage:
# put this script in top of FFmpeg source tree
# ./build_android
# It generates binary for following architectures:
# ARMv6 
# ARMv6+VFP 
# ARMv7+VFPv3-d16 (Tegra2) 
# ARMv7+Neon (Cortex-A8)
# Customizing:
# 1. Feel free to change ./configure parameters for more features
# 2. To adapt other ARM variants
# set $CPU and $OPTIMIZE_CFLAGS 
# call build_one
######################################################
export NDK=/Applications/adt-bundle-mac-x86_64/android-ndk-r10
PLATFORM=$NDK/platforms/android-L/arch-arm/
PREBUILT=$NDK/toolchains/arm-linux-androideabi-4.9/prebuilt/darwin-x86_64

function build_one
{
./configure \
--prefix=$PREFIX \
--enable-shared \
--disable-static \
--disable-doc \
--disable-ffmpeg \
--disable-ffplay \
--disable-ffprobe \
--disable-ffserver \
--disable-avdevice \
--disable-doc \
--disable-symver \
--disable-everything \
--enable-decoder=h264 \
--cross-prefix=$PREBUILT/bin/arm-linux-androideabi- \
--target-os=linux \
--arch=arm \
--enable-cross-compile \
--sysroot=$PLATFORM \
--extra-cflags="-Os -fpic $ADDI_CFLAGS" \
--extra-ldflags="$ADDI_LDFLAGS" \
$ADDITIONAL_CONFIGURE_FLAG

make clean
make
make install
}

CPU=arm
PREFIX=$(pwd)/android/$CPU
ADDI_CFLAGS="-marm"
build_one