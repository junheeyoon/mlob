from setuptools import setup, find_packages
import observability


setup(
    name="observability",
    description="observability live data import package",
    version=observability.__version__,
    author=observability.__author__,
    author_email="yoonjh4@asianaidt.com",
    packages=find_packages(exclude=[]),
    install_requires=["requests>=2.22.0"],
)