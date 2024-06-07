import os
import json

def split_json_file(file_path, output_dir, max_size_mb=25):
    with open(file_path, 'r', encoding='utf-8') as file:
        data = json.load(file)

    file_size = os.path.getsize(file_path) / (1024 * 1024)

    if file_size <= max_size_mb:
        # Copy the file as is
        with open(os.path.join(output_dir, os.path.basename(file_path)), 'w', encoding='utf-8') as output_file:
            json.dump(data, output_file, ensure_ascii=False, indent=4)
    else:
        # Split the file
        chunk_size = int(max_size_mb * 1024 * 1024 / 2)  # Approximate size for each chunk
        data_chunks = []
        current_chunk = []
        current_size = 0

        for item in data:
            item_size = len(json.dumps(item, ensure_ascii=False))
            if current_size + item_size > chunk_size:
                data_chunks.append(current_chunk)
                current_chunk = []
                current_size = 0
            current_chunk.append(item)
            current_size += item_size

        if current_chunk:
            data_chunks.append(current_chunk)

        for idx, chunk in enumerate(data_chunks):
            chunk_file_path = os.path.join(output_dir, f'{os.path.basename(file_path).split(".")[0]}_part{idx+1}.json')
            with open(chunk_file_path, 'w', encoding='utf-8') as chunk_file:
                json.dump(chunk, chunk_file, ensure_ascii=False, indent=4)

def process_directory(directory_path):
    for root, dirs, files in os.walk(directory_path):
        for file_name in files:
            if file_name.endswith('.json'):
                file_path = os.path.join(root, file_name)
                output_dir = os.path.join(root, os.path.splitext(file_name)[0])

                if not os.path.exists(output_dir):
                    os.makedirs(output_dir)

                split_json_file(file_path, output_dir)

# Exemple d'utilisation
directory_path = './'
process_directory(directory_path)
