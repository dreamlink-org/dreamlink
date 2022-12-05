#version 450

struct TextureMetaData {
    int texture_unit_id;
    int animation_total_frames;
    int animation_row_frames;
    int animation_start_frame;
    int animation_speed;
};

struct TerrainMetaData {
    int local_light;
    int portal_light;
    int is_hidden;
    int is_affected_by_light;
};

layout (location = 0) in vec3 position;
layout (location = 1) in vec4 texture_offset;
layout (location = 2) in int packed_texture_metadata;
layout (location = 3) in int packed_terrain_metadata;

uniform mat4 view_projection_matrix;
uniform mat4 view_rotation_matrix;
uniform mat4 view_translation_matrix;
uniform mat4 model_matrix;
uniform vec4 clip;
uniform int animation_frame;

out vec4 vert_screen_position;
out vec2 vert_texture_offset;
flat out TextureMetaData vert_texture_metadata;
flat out TerrainMetaData vert_terrain_metadata;

TextureMetaData unpack_texture_metadata(int packed_data) {
    TextureMetaData texture_metadata;
    texture_metadata.texture_unit_id = packed_data & 0xF;
    texture_metadata.animation_total_frames = (packed_data >> 4) & 0xFF;
    texture_metadata.animation_row_frames = (packed_data >> 12) & 0xFF;
    texture_metadata.animation_start_frame = (packed_data >> 20) & 0xFF;
    texture_metadata.animation_speed = 0x10 - (packed_data >> 28) & 0xF;
    return texture_metadata;
};

TerrainMetaData unpack_terrain_metadata(int packed_data) {
    TerrainMetaData terrain_metadata;
    terrain_metadata.local_light = packed_data & 0xF;
    terrain_metadata.portal_light = (packed_data >> 4) & 0xF;
    terrain_metadata.is_hidden = (packed_data >> 8) & 0x1;
    terrain_metadata.is_affected_by_light = (packed_data >> 9) & 0x1;
    return terrain_metadata;
};

void main() {
    vec4 world_position = model_matrix 
        * vec4(position, 1.0);

    vert_texture_metadata = unpack_texture_metadata(packed_texture_metadata);
    vert_terrain_metadata = unpack_terrain_metadata(packed_terrain_metadata);
    vert_screen_position = view_projection_matrix
        * view_rotation_matrix
        * view_translation_matrix
        * world_position;

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


    gl_Position = vert_screen_position;
    gl_ClipDistance[0] = dot(world_position, clip);
};
