"""Genera las texturas PNG del mod sin dependencias externas.  python3 tools/generate_textures.py"""
import zlib, struct, random, os, math

def write_png(path, w, h, px):
    raw = b''.join(b'\x00' + b''.join(bytes(px[y][x]) for x in range(w)) for y in range(h))
    def chunk(t, d):
        c = struct.pack('>I', len(d)) + t + d
        return c + struct.pack('>I', zlib.crc32(t + d) & 0xffffffff)
    data = b'\x89PNG\r\n\x1a\n' + chunk(b'IHDR', struct.pack('>IIBBBBB', w, h, 8, 6, 0, 0, 0))
    data += chunk(b'IDAT', zlib.compress(raw, 9)) + chunk(b'IEND', b'')
    os.makedirs(os.path.dirname(path), exist_ok=True)
    open(path, 'wb').write(data)

def blank(w, h): return [[(0, 0, 0, 0)] * w for _ in range(h)]
base = os.path.join(os.path.dirname(__file__), '..', 'src/main/resources/assets/ashenrealm/textures')

# ---------- Llave ----------
k = blank(16, 16)
for y in range(16):
    for x in range(16):
        d = math.hypot(x - 5, y - 5)
        if 2.2 <= d <= 4.4: k[y][x] = (150, 90, 210, 255) if d < 3.6 else (90, 50, 140, 255)
for i in range(7, 14):
    k[i][i] = (230, 190, 90, 255); k[i][i - 1] = (170, 130, 50, 255)
for (x, y) in [(11, 13), (12, 14), (9, 12), (13, 10), (14, 11)]:
    k[y][x] = (255, 140, 30, 255)
write_png(f'{base}/item/ashen_key.png', 16, 16, k)

# ---------- Núcleo ----------
c = blank(16, 16)
for y in range(16):
    for x in range(16):
        d = math.hypot(x - 7.5, y - 7.5)
        if d <= 6.2:
            t = d / 6.2
            col = (int(255 - 190 * t), int(150 - 110 * t), int(40 + 50 * t), 255)
            c[y][x] = col
c[4][5] = c[4][6] = c[5][4] = (255, 235, 190, 255)
write_png(f'{base}/item/ashen_core.png', 16, 16, c)

# ---------- Espada ----------
b = blank(16, 16)
for x in range(5, 15):
    y = 15 - x
    b[y][x] = (205, 175, 255, 255)
    if y + 1 < 16: b[y + 1][x] = (110, 70, 170, 255)
    if y - 1 >= 0: b[y - 1][x] = (60, 40, 95, 255) if x > 5 else b[y - 1][x]
for x, y in [(8, 7), (10, 5), (12, 3)]:
    b[y][x] = (255, 140, 30, 255)
for (x, y) in [(2, 9), (3, 10), (4, 11), (5, 12), (6, 13), (3, 11)]:
    b[y][x] = (230, 190, 90, 255)
for (x, y) in [(2, 13), (1, 14), (3, 12)]:
    b[y][x] = (90, 55, 40, 255)
b[15][0] = (60, 40, 30, 255)
write_png(f'{base}/item/ashen_blade.png', 16, 16, b)

# ---------- Entidad (layout humanoide 64x64) ----------
random.seed(7)
e = blank(64, 64)
def fill(x0, y0, x1, y1, col):
    for y in range(y0, y1):
        for x in range(x0, x1):
            n = random.randint(-10, 10)
            e[y][x] = (max(0, col[0] + n), max(0, col[1] + n), max(0, col[2] + n), 255)
skin = (48, 40, 58)
fill(0, 0, 32, 16, skin)        # cabeza
fill(16, 16, 40, 32, (38, 32, 48))  # torso
fill(40, 16, 56, 32, skin)      # brazo derecho
fill(0, 16, 16, 32, (34, 28, 44))   # pierna derecha
fill(32, 48, 48, 64, skin)      # brazo izquierdo
fill(16, 48, 32, 64, (34, 28, 44))  # pierna izquierda
# ojos y boca (cara frontal de la cabeza: x 8..15, y 8..15)
for (x, y) in [(9, 11), (10, 11), (13, 11), (14, 11)]:
    e[y][x] = (255, 150, 30, 255)
for x in range(10, 14):
    e[14][x] = (20, 14, 26, 255)
# grietas brillantes en el pecho (torso frontal x 20..27, y 20..31)
for (x, y) in [(21, 21), (22, 22), (22, 23), (23, 24), (24, 25), (24, 26), (25, 27), (23, 28), (22, 29)]:
    e[y][x] = (255, 120, 20, 255)
write_png(f'{base}/entity/ashen_warden.png', 64, 64, e)
print('texturas generadas')
