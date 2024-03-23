#version 410

struct TextureMetaData {
    int texture_unit_id;
    int animation_total_frames;
    int animation_row_frames;
    int animation_start_frame;
    int animation_speed;
};

layout (location = 0) in vec2 position;
layout (location = 1) in vec4 texture_offset;
layout (location = 2) in int packed_texture_metadata;
layout (location = 3) in int packed_color;

uniform int animation_frame;

out vec2 vert_texture_offset;
flat out TextureMetaData vert_texture_metadata;
flat out vec4 vert_color;

TextureMetaData unpack_texture_metadata(int packed_data) {
    TextureMetaData texture_metadata;
    texture_metadata.texture_unit_id = packed_data & 0xF;
    texture_metadata.animation_total_frames = (packed_data >> 4) & 0xFF;
    texture_metadata.animation_row_frames = (packed_data >> 12) & 0xFF;
    texture_metadata.animation_start_frame = (packed_data >> 20) & 0xFF;
    texture_metadata.animation_speed = 0x10 - (packed_data >> 28) & 0xF;
    return texture_metadata;
}

vec4 unpack_color(int packed_color) {
    vec4 color_data;
    color_data.a = packed_color & 0xFF;
    color_data.b = (packed_color >> 8) & 0xFF;
    color_data.g = (packed_color >> 16) & 0xFF;
    color_data.r = (packed_color >> 24) & 0xFF;
    color_data /= 255.0;
    return color_data;
}

void main() {
    vert_texture_metadata = unpack_texture_metadata(packed_texture_metadata);

    int offset_animation_frame = vert_texture_metadata.animation_start_frame;
    offset_animation_frame += animation_frame / vert_texture_metadata.animation_speed;
    offset_animation_frame %= vert_texture_metadata.animation_total_frames + 1;

    vec2 total_stride = vec2(
        texture_offset.z * (offset_animation_frame % (vert_texture_metadata.animation_row_frames + 1)),
        texture_offset.w * (offset_animation_frame / (vert_texture_metadata.animation_row_frames + 1))
    );

    vert_texture_offset = vec2(
        texture_offset.x + total_stride.x, 
        1.0 - texture_offset.y - total_stride.y
    );

    vert_color = unpack_color(packed_color);
    gl_Position = vec4(position, 0.0, 1.0);
}
